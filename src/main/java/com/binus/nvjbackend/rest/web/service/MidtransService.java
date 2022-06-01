package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Order;
import com.binus.nvjbackend.model.entity.OrderItem;
import com.binus.nvjbackend.rest.web.model.request.order.OrderRequest;
import com.midtrans.httpclient.error.MidtransError;

import java.util.List;

public interface MidtransService {

  Order.Midtrans createTransaction(OrderRequest request, List<OrderItem> orderItems)
      throws MidtransError;
}
