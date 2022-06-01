package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Order;
import com.binus.nvjbackend.rest.web.model.request.order.OrderRequest;
import com.midtrans.httpclient.error.MidtransError;

import java.text.ParseException;
import java.util.Map;

public interface OrderService {

  Order createOrder(OrderRequest request) throws MidtransError;

  Boolean isMidtransOrderIdExists(String id);

  void handleNotification(Map<String, Object> requestBody) throws ParseException;
}
