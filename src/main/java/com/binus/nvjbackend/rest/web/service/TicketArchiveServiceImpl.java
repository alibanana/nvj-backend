package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.TicketArchive;
import com.binus.nvjbackend.repository.TicketArchiveRepository;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketArchiveServiceImpl implements TicketArchiveService {

  @Autowired
  private TicketArchiveRepository ticketArchiveRepository;

  @Override
  public TicketArchive createAndReturnTicketArchive(TicketRequest request) {
    TicketArchive ticketArchive = new TicketArchive();
    BeanUtils.copyProperties(request, ticketArchive);
    return ticketArchiveRepository.save(ticketArchive);
  }
}
