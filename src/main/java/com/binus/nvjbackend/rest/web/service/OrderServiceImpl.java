package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.config.properties.SysparamProperties;
import com.binus.nvjbackend.model.entity.Order;
import com.binus.nvjbackend.model.entity.OrderItem;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.OrderRepository;
import com.binus.nvjbackend.rest.web.model.request.order.OrderRequest;
import com.binus.nvjbackend.rest.web.util.DateUtil;
import com.google.common.hash.Hashing;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

  @PostConstruct
  public void init() throws NoSuchAlgorithmException {
    md = MessageDigest.getInstance("SHA-512");
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
        .map(OrderItem::getPrice)
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
        dateUtil.toDateFromMidtrans((String) requestBody.get("transaction_time")));
    midtrans.setTransactionStatus((String) requestBody.get("transaction_status"));
    midtrans.setTransactionId((String) requestBody.get("transaction_id"));
    midtrans.setSettlementTime(
        dateUtil.toDateFromMidtrans((String) requestBody.get("settlement_time")));
    midtrans.setPaymentType((String) requestBody.get("settlement_type"));
    midtrans.setGrossAmount(Double.valueOf((String) requestBody.get("gross_amount")));
    midtrans.setFraudStatus((String) requestBody.get("fraud_status"));
    midtrans.setCurrency((String) requestBody.get("currency"));
    orderRepository.save(order);
  }
}
