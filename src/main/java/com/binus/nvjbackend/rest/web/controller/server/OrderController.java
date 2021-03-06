package com.binus.nvjbackend.rest.web.controller.server;

import com.binus.nvjbackend.model.entity.Order;
import com.binus.nvjbackend.model.entity.OrderItem;
import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.request.order.OrderFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.order.OrderRequest;
import com.binus.nvjbackend.rest.web.model.response.OrderItemResponse;
import com.binus.nvjbackend.rest.web.model.response.OrderResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestListResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestPageResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.OrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
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

  @PostMapping(value = ApiPath.ORDER_FIND_BY_FILTER)
  public RestPageResponse<OrderResponse> findByFilter(@RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size, @RequestParam(required = false) String orderBy,
      @RequestParam(required = false) String sortBy, @RequestBody OrderFilterRequest request) {
    Page<Order> orders = orderService.findByFilter(page, size, orderBy, sortBy, request);
    List<OrderResponse> content = orders.getContent().stream()
        .map(this::toOrderResponse)
        .collect(Collectors.toList());
    return toPageResponse(content, orders);
  }

  @PostMapping(value = ApiPath.ORDER_FIND_ALL)
  public RestListResponse<OrderResponse> findAll() {
    List<Order> orders = orderService.findAll();
    return toListResponse(orders.stream()
        .map(this::toOrderResponse)
        .collect(Collectors.toList()));
  }

  @PutMapping(value = ApiPath.ORDER_UPDATE_BY_ID)
  public RestSingleResponse<OrderResponse> updateById(@RequestParam String id,
      @Valid @RequestBody OrderRequest request) {
    Order order = orderService.updateManualOrderById(id, request);
    return toSingleResponse(toOrderResponse(order));
  }

  private OrderResponse toOrderResponse(Order order) {
    OrderResponse orderResponse = new OrderResponse();
    BeanUtils.copyProperties(order, orderResponse);
    orderResponse.setOrderItems(order.getOrderItems().stream()
        .map(this::toOrderItemResponse)
        .collect(Collectors.toList()));
    if (!order.getIsManualOrder()) {
      orderResponse.setMidtrans(toMidtransResponse(order.getMidtrans()));
    }
    return orderResponse;
  }

  private OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
    return OrderItemResponse.builder()
        .id(orderItem.getId())
        .ticketId(orderItem.getTicket().getId())
        .title(orderItem.getTicket().getTitle())
        .quantity(orderItem.getQuantity())
        .price(orderItem.getPrice())
        .build();
  }

  private OrderResponse.Midtrans toMidtransResponse(Order.Midtrans midtrans) {
    OrderResponse.Midtrans midtransResponse = new OrderResponse.Midtrans();
    BeanUtils.copyProperties(midtrans, midtransResponse);
    return midtransResponse;
  }
}
