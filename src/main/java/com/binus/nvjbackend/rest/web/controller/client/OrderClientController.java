package com.binus.nvjbackend.rest.web.controller.client;

import com.binus.nvjbackend.model.entity.Order;
import com.binus.nvjbackend.model.entity.OrderItem;
import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.ApiPathClient;
import com.binus.nvjbackend.rest.web.model.request.order.OrderRequest;
import com.binus.nvjbackend.rest.web.model.response.OrderClientResponse;
import com.binus.nvjbackend.rest.web.model.response.OrderItemClientResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.OrderService;
import com.midtrans.httpclient.error.MidtransError;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Map;
import java.util.stream.Collectors;

@Api(value = "Client - Orders", description = "Client - Orders Service API")
@RestController
@RequestMapping(value = ApiPathClient.BASE_PATH_ORDER)
public class OrderClientController extends BaseController {

  @Autowired
  private OrderService orderService;

  @PostMapping(value = ApiPathClient.ORDER_CREATE)
  public RestSingleResponse<OrderClientResponse> createOrder(
      @Valid @RequestBody OrderRequest request) throws MidtransError {
    Order order = orderService.createOrder(request);
    return toSingleResponse(toOrderClientResponse(order));
  }

  @PostMapping(ApiPathClient.ORDER_HANDLE_NOTIFICATION)
  public RestBaseResponse handleNotification(@RequestBody Map<String, Object> requestBody)
      throws ParseException {
    orderService.handleNotification(requestBody);
    return toBaseResponse();
  }

  private OrderClientResponse toOrderClientResponse(Order order) {
    return OrderClientResponse.builder()
        .id(order.getId())
        .firstname(order.getFirstname())
        .lastname(order.getLastname())
        .email(order.getEmail())
        .visitDate(order.getVisitDate())
        .totalPrice(order.getTotalPrice())
        .orderItems(order.getOrderItems().stream()
            .map(this::toOrderItemClientResponse)
            .collect(Collectors.toList()))
        .midtrans(toOrderClientMidtransResponse(order.getMidtrans()))
        .build();
  }

  private OrderItemClientResponse toOrderItemClientResponse(OrderItem orderItem) {
    return OrderItemClientResponse.builder()
        .id(orderItem.getId())
        .title(orderItem.getTicket().getTitle())
        .description(orderItem.getTicket().getDescription())
        .quantity(orderItem.getQuantity())
        .price(orderItem.getPrice())
        .build();
  }

  private OrderClientResponse.Midtrans toOrderClientMidtransResponse(Order.Midtrans midtrans) {
    return OrderClientResponse.Midtrans.builder()
        .orderId(midtrans.getOrderId())
        .token(midtrans.getToken())
        .redirectUrl(midtrans.getRedirectUrl())
        .build();
  }
}
