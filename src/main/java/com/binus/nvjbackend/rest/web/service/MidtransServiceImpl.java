package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.config.properties.SysparamProperties;
import com.binus.nvjbackend.model.entity.Order;
import com.binus.nvjbackend.model.entity.OrderItem;
import com.binus.nvjbackend.rest.web.model.request.order.OrderRequest;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MidtransServiceImpl implements MidtransService {

  @Autowired
  private SysparamProperties sysparamProperties;

  @Autowired
  private OrderService orderService;

  @PostConstruct
  public void init() {
    Midtrans.serverKey = sysparamProperties.getMidtransServerKey();
    Midtrans.isProduction = sysparamProperties.getMidtransIsProduction();
  }

  @Override
  public Order.Midtrans createTransaction(OrderRequest request, List<OrderItem> orderItems)
      throws MidtransError {
    String midtransOrderId = generateMidtransOrderId();
    Map<String, Object> params = buildRequestBody(request, orderItems, midtransOrderId);
    JSONObject response = SnapApi.createTransaction(params);
    return Order.Midtrans.builder()
        .orderId(midtransOrderId)
        .token((String) response.get("token"))
        .redirectUrl((String) response.get("redirect_url"))
        .build();
  }

  private String generateMidtransOrderId() {
    String id = UUID.randomUUID().toString();
    while (orderService.isMidtransOrderIdExists(id)) {
      id = UUID.randomUUID().toString();
    }
    return id;
  }

  private Map<String, Object> buildRequestBody(OrderRequest request, List<OrderItem> orderItems,
      String midtransOrderId) {
    Map<String, Object> params = new HashMap<>();
    params.put("transaction_details", buildTransactionDetails(orderItems, midtransOrderId));
    params.put("item_details", buildItemDetails(orderItems));
    params.put("customer_details", buildCustomerDetails(request));
    return params;
  }

  private Map<String, String> buildTransactionDetails(List<OrderItem> orderItems,
      String midtransOrderId) {
    Map<String, String> transactionDetails = new HashMap<>();
    transactionDetails.put("order_id", midtransOrderId);
    transactionDetails.put("gross_amount", String.valueOf(getGrossAmount(orderItems)));
    return transactionDetails;
  }

  private Double getGrossAmount(List<OrderItem> orderItems) {
    return orderItems.stream()
        .map(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
        .reduce(0.0, Double::sum);
  }

  private List<Map<String, Object>> buildItemDetails(List<OrderItem> orderItems) {
    return orderItems.stream()
        .map(this::buildItemDetail)
        .collect(Collectors.toList());
  }

  private Map<String, Object> buildItemDetail(OrderItem orderItem) {
    Map<String, Object> itemDetail = new HashMap<>();
    itemDetail.put("id", orderItem.getId());
    itemDetail.put("price", orderItem.getPrice());
    itemDetail.put("quantity", orderItem.getQuantity());
    itemDetail.put("name", orderItem.getTicket().getTitle());
    return itemDetail;
  }

  private Map<String, String> buildCustomerDetails(OrderRequest request) {
    Map<String, String> customerDetails = new HashMap<>();
    customerDetails.put("first_name", request.getFirstname());
    customerDetails.put("last_name", request.getLastname());
    customerDetails.put("email", request.getEmail());
    return customerDetails;
  }
}
