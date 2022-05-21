package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Ticket;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketRequest;
import org.springframework.data.domain.Page;

public interface TicketService {

  Ticket create(TicketRequest request);

  Page<Ticket> findByFilter(Integer page, Integer size, String orderBy, String sortBy,
      TicketFilterRequest request);

  Ticket updateByTitle(TicketRequest request);
}
