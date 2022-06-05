package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Ticket;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TicketService {

  Ticket create(TicketRequest request);

  List<Ticket> findAllWithSorting(String orderBy, String sortBy);

  Page<Ticket> findByFilter(Integer page, Integer size, String orderBy, String sortBy,
      TicketFilterRequest request, Boolean isClientApi);

  List<Ticket> findByIds(List<String> ids);

  Ticket updateById(String id, TicketRequest request);

  void deleteById(String id);

  void validateTicketsPurchasable(List<Ticket> tickets);
}
