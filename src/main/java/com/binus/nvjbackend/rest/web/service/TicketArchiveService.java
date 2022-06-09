package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.TicketArchive;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketRequest;

import java.util.List;
import java.util.Map;

public interface TicketArchiveService {

  TicketArchive createAndReturnTicketArchive(TicketRequest request, int version);

  Map<String, TicketArchive> findTicketArchivesByTicketIdAndVersionMap(
      Map<String, Integer> ticketIdAndVersionMap);
}
