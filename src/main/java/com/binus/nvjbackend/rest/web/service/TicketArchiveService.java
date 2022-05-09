package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.TicketArchive;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketRequest;

public interface TicketArchiveService {

  TicketArchive createAndReturnTicketArchive(TicketRequest request, int version);
}
