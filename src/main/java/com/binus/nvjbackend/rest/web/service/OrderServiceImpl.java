package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.config.properties.SysparamProperties;
import com.binus.nvjbackend.model.entity.Order;
import com.binus.nvjbackend.model.entity.OrderItem;
import com.binus.nvjbackend.model.entity.Ticket;
import com.binus.nvjbackend.model.entity.TicketArchive;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.OrderRepository;
import com.binus.nvjbackend.rest.web.model.request.order.OrderClientRequest;
import com.binus.nvjbackend.rest.web.model.request.order.OrderFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.order.OrderItemRequest;
import com.binus.nvjbackend.rest.web.model.request.order.OrderRequest;
import com.binus.nvjbackend.rest.web.util.DateUtil;
import com.binus.nvjbackend.rest.web.util.EmailTemplateUtil;
import com.binus.nvjbackend.rest.web.util.OtherUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.midtrans.httpclient.error.MidtransError;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  private SysparamProperties sysparamProperties;

  @Autowired
  private OrderItemService orderItemService;

  @Autowired
  private MidtransService midtransService;

  @Autowired
  private TicketArchiveService ticketArchiveService;

  @Autowired
  private EmailTemplateService emailTemplateService;

  @Autowired
  private TicketService ticketService;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private DateUtil dateUtil;

  @Autowired
  private OtherUtil otherUtil;

  @Autowired
  private EmailTemplateUtil emailTemplateUtil;

  private ObjectMapper oMapper;

  @PostConstruct
  public void init() {
    oMapper = new ObjectMapper();
  }

  @Override
  public Order createOrder(OrderRequest request) {
    otherUtil.validateEmail(request.getEmail());
    otherUtil.validatePhoneNumber(request.getPhoneNumber());
    validateVisitDate(request.getVisitDate());
    List<OrderItem> orderItems = orderItemService.createOrderItems(request.getOrderItems());
    return orderRepository.save(buildOrder(request, orderItems));
  }

  @Override
  public Order createClientOrder(OrderClientRequest request) throws MidtransError,
      TemplateException, MessagingException, IOException {
    otherUtil.validateEmail(request.getEmail());
    otherUtil.validatePhoneNumber(request.getPhoneNumber());
    validateVisitDate(request.getVisitDate());
    List<OrderItem> orderItems = orderItemService.createOrderItems(request.getOrderItems());
    Order.Midtrans midtrans = midtransService.createTransaction(request, orderItems);
    Order order = orderRepository.save(buildClientOrder(request, orderItems, midtrans));
    checkMidtransTransactionStatusAndSendEmail(order);
    return order;
  }

  @Override
  public Boolean isMidtransOrderIdExists(String id) {
    return orderRepository.existsByMidtransOrderId(id);
  }

  @Override
  public void handleNotification(Map<String, Object> requestBody) throws ParseException,
      TemplateException, MessagingException, IOException {
    validateRequestBody(requestBody);
    Order order = orderRepository.findByMidtransOrderId((String) requestBody.get("order_id"));
    if (Objects.isNull(order)) {
      throw new BaseException(ErrorCode.MIDTRANS_ORDER_ID_NOT_FOUND);
    }
    validateSignatureKey(requestBody, order);
    Order updatedOrder = updateOrderToMongo(requestBody, order);
    checkMidtransTransactionStatusAndSendEmail(updatedOrder);
  }

  @Override
  public Order findByMidtransOrderId(String midtransOrderId) {
    Order order = orderRepository.findByMidtransOrderId(midtransOrderId);
    if (Objects.isNull(order)) {
      throw new BaseException(ErrorCode.MIDTRANS_ORDER_ID_NOT_FOUND);
    }
    return order;
  }

  @Override
  public List<Order> findAll() {
    return orderRepository.findAll();
  }

  @Override
  public Page<Order> findByFilter(Integer page, Integer size, String orderBy, String sortBy,
      OrderFilterRequest request) {
    PageRequest pageRequest = otherUtil.validateAndGetPageRequest(page, size, orderBy, sortBy);
    return orderRepository.findAllByFilter(request.getId(), request.getFirstname(),
        request.getLastname(), request.getEmail(), request.getPhoneNumber(),
        request.getFromVisitDate(), request.getToVisitDate(), request.getPaymentType(),
        request.getIsManualOrder(), request.getMidtransOrderId(),
        request.getMidtransTransactionStatus(), pageRequest);
  }

  @Override
  public Order updateManualOrderById(String id, OrderRequest request) {
    Order order = Optional.of(orderRepository.findById(id)).get()
        .orElse(null);
    if (Objects.isNull(order)) {
      throw new BaseException(ErrorCode.ORDER_ID_NOT_FOUND);
    } else if (!order.getIsManualOrder()) {
      throw new BaseException(ErrorCode.ORDER_MUST_BE_MANUAL_ORDER);
    }
    return updateManualOrder(order, request);
  }

  @Override
  public void resendEmailByMidtransOrderId(String midtransOrderId) throws TemplateException,
      MessagingException, IOException {
    Order order = orderRepository.findByMidtransOrderId(midtransOrderId);
    if (Objects.isNull(order)) {
      throw new BaseException(ErrorCode.MIDTRANS_ORDER_ID_NOT_FOUND);
    }
    checkMidtransTransactionStatusAndSendEmail(order);
  }

  @Override
  public Map<String, Object> getWeeklyOrderData() {
    Date to = dateUtil.getDateOnlyForToday();
    Date from = DateUtils.addDays(to, -7);
    List<Double> orderValues = new ArrayList<>();
    List<Integer> orderCounts = new ArrayList<>();
    List<Integer> ticketCounts = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      Date currentDate = DateUtils.addDays(from, i);
      Date nextDate = DateUtils.addDays(from, i + 1);
      List<Order> orders = orderRepository.findOrderByCreationDate(currentDate, nextDate);
      orderValues.add(orders.stream().mapToDouble(Order::getTotalPrice).sum());
      orderCounts.add(orders.size());
      ticketCounts.add(orders.stream()
          .map(order -> (Integer) order.getOrderItems().stream()
              .map(OrderItem::getQuantity)
              .mapToInt(Integer::intValue)
              .sum())
          .mapToInt(Integer::intValue)
          .sum());
    }
    Map<String, Object> weeklyOrderData = new HashMap<>();
    weeklyOrderData.put("orderValues", orderValues);
    weeklyOrderData.put("orderCounts", orderCounts);
    weeklyOrderData.put("ticketCounts", ticketCounts);
    return weeklyOrderData;
  }

  private void validateVisitDate(Date date) {
    if (dateUtil.isDateBeforeToday(date)) {
      throw new BaseException(ErrorCode.ORDER_VISIT_DATE_BEFORE_TODAY);
    }
  }

  private Order buildOrder(OrderRequest request, List<OrderItem> orderItems) {
    return Order.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .description(request.getDescription())
        .phoneNumber(request.getPhoneNumber())
        .visitDate(request.getVisitDate())
        .totalPrice(getTotalPrice(orderItems))
        .paymentType(request.getPaymentType())
        .isManualOrder(Boolean.TRUE)
        .orderItems(orderItems)
        .build();
  }

  private Order buildClientOrder(OrderClientRequest request, List<OrderItem> orderItems,
      Order.Midtrans midtrans) {
    return Order.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .phoneNumber(request.getPhoneNumber())
        .visitDate(request.getVisitDate())
        .totalPrice(getTotalPrice(orderItems))
        .midtrans(midtrans)
        .isManualOrder(Boolean.FALSE)
        .orderItems(orderItems)
        .build();
  }

  private Order updateManualOrder(Order order, OrderRequest request) {
    BeanUtils.copyProperties(request, order);
    List<OrderItem> updatedOrderItems = updateExistingOrderItems(request.getOrderItems(), order);
    order.setOrderItems(updatedOrderItems);
    List<OrderItem> newOrderItems = createNewOrderItems(request.getOrderItems(),
        order.getOrderItems());
    order.getOrderItems().addAll(newOrderItems);
    return orderRepository.save(order);
  }

  private List<OrderItem> updateExistingOrderItems(List<OrderItemRequest> orderItemRequests, Order order) {
    Map<String, Integer> requestTicketIdAndQuantityMap = orderItemRequests.stream()
        .collect(Collectors.toMap(OrderItemRequest::getTicketId, OrderItemRequest::getQuantity));
    List<OrderItem> updatedOrderItemList = new ArrayList<>();
    for (OrderItem orderItem : order.getOrderItems()) {
      Ticket ticket = ticketService.findById(orderItem.getTicket().getId());
      if (requestTicketIdAndQuantityMap.containsKey(orderItem.getTicket().getId())
          && orderItem.getTicketVersion().equals(ticket.getVersion())) {
        if (!requestTicketIdAndQuantityMap.get(orderItem.getTicket().getId())
            .equals(orderItem.getQuantity())) {
          orderItem.setQuantity(requestTicketIdAndQuantityMap.get(orderItem.getTicket().getId()));
        }
        updatedOrderItemList.add(orderItemService.updateOrderItem(orderItem));
      } else {
        orderItemService.deleteById(orderItem.getId());
      }
    }
    return updatedOrderItemList;
  }

  private List<OrderItem> createNewOrderItems(List<OrderItemRequest> orderItemRequests,
      List<OrderItem> orderItems) {
    Map<String, Integer> existingTicketIdAndVersionMap = orderItems.stream()
        .collect(Collectors.toMap(orderItem -> orderItem.getTicket().getId(),
            OrderItem::getTicketVersion));
    List<OrderItemRequest> newOrderItemRequests = new ArrayList<>();
    for (OrderItemRequest orderItemRequest : orderItemRequests) {
      Ticket ticket = ticketService.findById(orderItemRequest.getTicketId());
      if (!existingTicketIdAndVersionMap.containsKey(orderItemRequest.getTicketId())) {
        newOrderItemRequests.add(orderItemRequest);
      } else if (!existingTicketIdAndVersionMap.get(orderItemRequest.getTicketId())
          .equals(ticket.getVersion())) {
        newOrderItemRequests.add(orderItemRequest);
      }
    }
    return newOrderItemRequests.isEmpty() ? new ArrayList<>() :
        orderItemService.createOrderItems(newOrderItemRequests);
  }

  private void checkMidtransTransactionStatusAndSendEmail(Order order)
      throws TemplateException, MessagingException, IOException {
    Map<String, TicketArchive> ticketIdAndTicketArchiveMap =
        ticketArchiveService.findTicketArchivesByTicketIdAndVersionMap(
            generateTicketIdAndVersionMap(order));

    if (Objects.isNull(order.getMidtrans().getTransactionStatus())) {
      emailTemplateService.sendEmailTemplate(
          emailTemplateUtil.buildWaitingForPaymentEmail(order, ticketIdAndTicketArchiveMap));
    } else if (order.getMidtrans().getTransactionStatus().equals("settlement")) {
      emailTemplateService.sendEmailTemplate(
          emailTemplateUtil.buildPaymentSuccessEmail(order, ticketIdAndTicketArchiveMap));
    } else if (order.getMidtrans().getTransactionStatus().equals("expire")) {
      emailTemplateService.sendEmailTemplate(
          emailTemplateUtil.buildPaymentExpiredEmail(order, ticketIdAndTicketArchiveMap));
    }
  }

  private Map<String, Integer> generateTicketIdAndVersionMap(Order order) {
    Map<String, Integer> map = new HashMap<>();
    for (OrderItem orderItem : order.getOrderItems()) {
      map.put(orderItem.getTicket().getId(), orderItem.getTicketVersion());
    }
    return map;
  }

  private Double getTotalPrice(List<OrderItem> orderItems) {
    return orderItems.stream()
        .map(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
        .reduce(0.0, Double::sum);
  }

  private void validateRequestBody(Map<String, Object> requestBody) {
    if (!requestBody.containsKey("order_id")) {
      throw new BaseException(ErrorCode.ORDER_ID_MISSING);
    } else if (!requestBody.containsKey("signature_key")) {
      throw new BaseException(ErrorCode.ORDER_SIGNATURE_KEY_MISSING);
    }
  }

  private void validateSignatureKey(Map<String, Object> requestBody, Order order) {
    String signatureKey = (String) requestBody.get("signature_key");
    String stringToHash = order.getMidtrans().getOrderId() + requestBody.get("status_code") +
        requestBody.get("gross_amount") + sysparamProperties.getMidtransServerKey();
    if (!signatureKey.equals(Hashing.sha512().hashString(stringToHash, StandardCharsets.UTF_8)
        .toString())) {
      throw new BaseException(ErrorCode.ORDER_SIGNATURE_KEY_INVALID);
    }
  }

  private Order updateOrderToMongo(Map<String, Object> requestBody, Order order)
      throws ParseException {
    Order.Midtrans midtrans = order.getMidtrans();
    midtrans.setTransactionTime(
        dateUtil.toDateFromMidtrans((String) requestBody.getOrDefault("transaction_time", null)));
    midtrans.setTransactionStatus((String) requestBody.getOrDefault("transaction_status", null));
    midtrans.setTransactionId((String) requestBody.getOrDefault("transaction_id", null));
    midtrans.setStatusMessage((String) requestBody.getOrDefault("status_message", null));
    midtrans.setStatusCode((String) requestBody.getOrDefault("status_code", null));
    midtrans.setSignatureKey((String) requestBody.getOrDefault("signature_key", null));
    midtrans.setPaymentType((String) requestBody.getOrDefault("payment_type", null));
    midtrans.setMerchantId((String) requestBody.getOrDefault("merchant_id", null));
    midtrans.setSettlementTime(
        dateUtil.toDateFromMidtrans((String) requestBody.getOrDefault("settlement_time", null)));
    midtrans.setGrossAmount(Double.valueOf((String) requestBody.getOrDefault("gross_amount", null)));
    midtrans.setFraudStatus((String) requestBody.getOrDefault("fraud_status", null));
    midtrans.setCurrency((String) requestBody.getOrDefault("currency", null));
    // For Credit Cards
    midtrans.setMaskedCard((String) requestBody.getOrDefault("masked_card", null));
    midtrans.setEci((String) requestBody.getOrDefault("eci", null));
    midtrans.setChannelResponseMessage((String) requestBody
        .getOrDefault("channel_response_message", null));
    midtrans.setChannelResponseCode((String) requestBody
        .getOrDefault("channel_response_code", null));
    midtrans.setCardType((String) requestBody.getOrDefault("card_type", null));
    midtrans.setBank((String) requestBody.getOrDefault("bank", null));
    midtrans.setApprovalCode((String) requestBody.getOrDefault("approval_code", null));
    // For QRIS
    midtrans.setAcquirer((String) requestBody.getOrDefault("acquirer", null));
    // For Mandiri Bill
    midtrans.setBillerCode((String) requestBody.getOrDefault("biller_code", null));
    midtrans.setBillKey((String) requestBody.getOrDefault("bill_key", null));
    // For Virtual Accounts
    midtrans.setPermataVaNumber((String) requestBody.getOrDefault("permataVaNumber", null));
    midtrans.setVaNumbers(toVaNumbers(requestBody.getOrDefault("va_numbers", new ArrayList<>())));
    midtrans.setPaymentAmounts(toPaymentAmounts(
        requestBody.getOrDefault("payment_amounts", new ArrayList<>())));
    return orderRepository.save(order);
  }

  private List<Order.Midtrans.VaNumber> toVaNumbers(Object vaNumbers) {
    List<Map<String, Object>> list = oMapper.convertValue(vaNumbers,
        new TypeReference<List<Map<String, Object>>>() {});
    return list.isEmpty() ? null : list.stream()
        .map(map -> Order.Midtrans.VaNumber.builder()
            .vaNumber((String) map.getOrDefault("va_number", null))
            .bank((String) map.getOrDefault("bank", null))
            .build())
        .collect(Collectors.toList());
  }

  private List<Order.Midtrans.PaymentAmount> toPaymentAmounts(Object paymentAmounts) {
    List<Map<String, Object>> list = oMapper.convertValue(paymentAmounts,
        new TypeReference<List<Map<String, Object>>>() {});
    return list.isEmpty() ? null : list.stream()
        .map(map -> {
          try {
            return toPaymentAmount(map);
          } catch (ParseException e) {
            e.printStackTrace();
          }
          return null;
        })
        .collect(Collectors.toList());
  }

  private Order.Midtrans.PaymentAmount toPaymentAmount(Map<String, Object> map)
      throws ParseException {
    return Order.Midtrans.PaymentAmount.builder()
        .paidAt(dateUtil.toDateFromMidtrans(
            (String) map.getOrDefault("paid_at", null)))
        .amount(Double.valueOf((String) map.getOrDefault("amount", null)))
        .build();
  }
}
