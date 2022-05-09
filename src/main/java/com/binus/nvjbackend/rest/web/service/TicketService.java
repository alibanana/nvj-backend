package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Ticket;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketRequest;

public interface TicketService {

  Ticket create(TicketRequest request);

  Ticket updateByTitle(TicketRequest request);
}
