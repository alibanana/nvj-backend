package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.OrderItem;
import com.binus.nvjbackend.rest.web.model.request.order.OrderItemRequest;

import java.util.List;

public interface OrderItemService {

  List<OrderItem> createOrderItems(List<OrderItemRequest> requests);
}
