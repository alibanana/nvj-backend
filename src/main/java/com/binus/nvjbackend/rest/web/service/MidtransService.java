package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Order;
import com.binus.nvjbackend.model.entity.OrderItem;
import com.binus.nvjbackend.rest.web.model.request.order.OrderClientRequest;
import com.midtrans.httpclient.error.MidtransError;

import java.util.List;

public interface MidtransService {

  Order.Midtrans createTransaction(OrderClientRequest request, List<OrderItem> orderItems)
      throws MidtransError;
}
