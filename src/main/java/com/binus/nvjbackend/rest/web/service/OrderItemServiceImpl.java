package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.OrderItem;
import com.binus.nvjbackend.model.entity.Ticket;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.OrderItemRepository;
import com.binus.nvjbackend.rest.web.model.request.order.OrderItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {

  @Autowired
  private TicketService ticketService;

  @Autowired
  private OrderItemRepository orderItemRepository;

  @Override
  public List<OrderItem> createOrderItems(List<OrderItemRequest> requests) {
    List<String> ticketIds = getTicketIdsFromRequests(requests);
    List<Ticket> tickets = ticketService.findByIds(ticketIds);
    ticketService.validateTicketsPurchasable(tickets);
    Map<String, Integer> ticketIdAndQuantityMap = generateTicketIdAndQuantityMap(requests);
    Map<String, Ticket> ticketIdAndTicketMap = generateTicketIdAndTicketMap(tickets);
    List<OrderItem> orderItems = ticketIds.stream()
        .map(id -> buildOrderItem(id, ticketIdAndQuantityMap, ticketIdAndTicketMap))
        .collect(Collectors.toList());
    return orderItemRepository.saveAll(orderItems);
  }

  @Override
  public OrderItem updateOrderItem(OrderItem orderItem) {
    return orderItemRepository.save(orderItem);
  }

  @Override
  public void deleteById(String id) {
    orderItemRepository.deleteById(id);
  }

  private List<String> getTicketIdsFromRequests(List<OrderItemRequest> requests) {
    return requests.stream()
        .map(OrderItemRequest::getTicketId)
        .collect(Collectors.toList());
  }

  private Map<String, Integer> generateTicketIdAndQuantityMap(List<OrderItemRequest> requests) {
    return requests.stream()
        .collect(Collectors.toMap(OrderItemRequest::getTicketId, OrderItemRequest::getQuantity));
  }

  private Map<String, Ticket> generateTicketIdAndTicketMap(List<Ticket> tickets) {
    return tickets.stream()
        .collect(Collectors.toMap(Ticket::getId, Function.identity()));
  }

  private OrderItem buildOrderItem(String id, Map<String, Integer> ticketIdAndQuantityMap,
      Map<String, Ticket> ticketIdAndTicketMap) {
    Ticket ticket = ticketIdAndTicketMap.get(id);
    return OrderItem.builder()
        .quantity(ticketIdAndQuantityMap.get(id))
        .price(ticket.getPrice())
        .ticket(ticket)
        .ticketVersion(ticket.getVersion())
        .build();
  }
}
