package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Ticket;
import com.binus.nvjbackend.model.entity.TicketArchive;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.TicketRepository;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
public class TicketServiceImpl implements TicketService {

  @Autowired
  private TicketArchiveService ticketArchiveService;

  @Autowired
  private TicketRepository ticketRepository;

  @Override
  public Ticket create(TicketRequest request) {
    validateTicketDoesNotExistsByTitle(request.getTitle());
    TicketArchive ticketArchive = ticketArchiveService.createAndReturnTicketArchive(request, 0);
    return ticketRepository.save(initializeNewTicket(ticketArchive));
  }

  @Override
  public Ticket updateByTitle(TicketRequest request) {
    Ticket ticket = ticketRepository.findByTitle(request.getTitle());
    if (Objects.isNull(ticket)) {
      throw new BaseException(ErrorCode.TICKET_NOT_FOUND);
    }
    return updateTicketAndTicketArchives(ticket, request);
  }

  private void validateTicketDoesNotExistsByTitle(String title) {
    if (ticketRepository.existsByTitle(title)) {
      throw new BaseException(ErrorCode.TICKET_ALREADY_EXISTS);
    }
  }

  private Ticket initializeNewTicket(TicketArchive ticketArchive) {
    Ticket ticket = new Ticket();
    BeanUtils.copyProperties(ticketArchive, ticket);
    ticket.setVersion(null); // let version be handled by springboot.
    ticket.setTicketArchives(Collections.singletonList(ticketArchive));
    return ticket;
  }

  private Ticket updateTicketAndTicketArchives(Ticket ticket, TicketRequest request) {
    ticket.setDescription(request.getDescription());
    ticket.setPrice(request.getPrice());
    TicketArchive ticketArchive = ticketArchiveService.createAndReturnTicketArchive(request,
        ticket.getVersion() + 1);
    ticket.getTicketArchives().add(ticketArchive);
    return ticketRepository.save(ticket);
  }
}
