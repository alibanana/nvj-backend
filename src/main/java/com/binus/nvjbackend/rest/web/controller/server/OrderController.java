package com.binus.nvjbackend.rest.web.controller.server;

import com.binus.nvjbackend.model.entity.Order;
import com.binus.nvjbackend.model.entity.OrderItem;
import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.request.order.OrderRequest;
import com.binus.nvjbackend.rest.web.model.response.OrderItemResponse;
import com.binus.nvjbackend.rest.web.model.response.OrderResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.OrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.stream.Collectors;

@Api(value = "Orders", description = "Orders Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_ORDER)
public class OrderController extends BaseController {

  @Autowired
  private OrderService orderService;

  @PostMapping(value = ApiPath.ORDER_CREATE)
  public RestSingleResponse<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
    Order order = orderService.createOrder(request);
    return toSingleResponse(toOrderResponse(order));
  }

  private OrderResponse toOrderResponse(Order order) {
    OrderResponse orderResponse = new OrderResponse();
    BeanUtils.copyProperties(order, orderResponse);
    orderResponse.setOrderItems(order.getOrderItems().stream()
        .map(this::toOrderItemResponse)
        .collect(Collectors.toList()));
    return orderResponse;
  }

  private OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
    return OrderItemResponse.builder()
        .id(orderItem.getId())
        .title(orderItem.getTicket().getTitle())
        .quantity(orderItem.getQuantity())
        .price(orderItem.getPrice())
        .build();
  }
}