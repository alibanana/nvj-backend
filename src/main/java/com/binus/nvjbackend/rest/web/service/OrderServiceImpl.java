package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.config.properties.SysparamProperties;
import com.binus.nvjbackend.model.entity.Order;
import com.binus.nvjbackend.model.entity.OrderItem;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.OrderRepository;
import com.binus.nvjbackend.rest.web.model.request.order.OrderRequest;
import com.binus.nvjbackend.rest.web.util.DateUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
  private OrderRepository orderRepository;

  @Autowired
  private DateUtil dateUtil;

  private MessageDigest md;

  private ObjectMapper oMapper;

  @PostConstruct
  public void init() throws NoSuchAlgorithmException {
    md = MessageDigest.getInstance("SHA-512");
    oMapper = new ObjectMapper();
  }

  @Override
  public Order createOrder(OrderRequest request) throws MidtransError {
    List<OrderItem> orderItems = orderItemService.createOrderItems(request.getOrderItems());
    Order.Midtrans midtrans = midtransService.createTransaction(request, orderItems);
    return orderRepository.save(buildOrder(request, orderItems, midtrans));
  }

  @Override
  public Boolean isMidtransOrderIdExists(String id) {
    return orderRepository.existsByMidtransOrderId(id);
  }

  @Override
  public void handleNotification(Map<String, Object> requestBody) throws ParseException {
    validateRequestBody(requestBody);
    Order order = orderRepository.findByMidtransOrderId((String) requestBody.get("order_id"));
    if (Objects.isNull(order)) {
      throw new BaseException(ErrorCode.MIDTRANS_ORDER_ID_NOT_FOUND);
    }
    validateSignatureKey(requestBody, order);
    updateOrderToMongo(requestBody, order);
  }

  private Order buildOrder(OrderRequest request, List<OrderItem> orderItems,
      Order.Midtrans midtrans) {
    return Order.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .visitDate(request.getVisitDate())
        .totalPrice(getTotalPrice(orderItems))
        .midtrans(midtrans)
        .orderItems(orderItems)
        .build();
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

  private void updateOrderToMongo(Map<String, Object> requestBody, Order order)
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
    orderRepository.save(order);
  }

  private List<Order.Midtrans.VaNumber> toVaNumbers(Object vaNumbers) {
    List<Map<String, Object>> list = oMapper.convertValue(vaNumbers,
        new TypeReference<List<Map<String, Object>>>() {});
    return list.stream()
        .map(map -> Order.Midtrans.VaNumber.builder()
            .vaNumber((String) map.getOrDefault("va_number", null))
            .bank((String) map.getOrDefault("bank", null))
            .build())
        .collect(Collectors.toList());
  }

  private List<Order.Midtrans.PaymentAmount> toPaymentAmounts(Object paymentAmounts) {
    List<Map<String, Object>> list = oMapper.convertValue(paymentAmounts,
        new TypeReference<List<Map<String, Object>>>() {});
    return list.stream()
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
