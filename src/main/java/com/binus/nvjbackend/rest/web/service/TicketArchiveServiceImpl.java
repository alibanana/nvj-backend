package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Ticket;
import com.binus.nvjbackend.model.entity.TicketArchive;
import com.binus.nvjbackend.repository.TicketArchiveRepository;
import com.binus.nvjbackend.repository.TicketRepository;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TicketArchiveServiceImpl implements TicketArchiveService {

  @Autowired
  private TicketArchiveRepository ticketArchiveRepository;

  @Autowired
  private TicketRepository ticketRepository;

  @Override
  public TicketArchive createAndReturnTicketArchive(TicketRequest request, int version) {
    TicketArchive ticketArchive = new TicketArchive();
    BeanUtils.copyProperties(request, ticketArchive);
    ticketArchive.setVersion(version);
    return ticketArchiveRepository.save(ticketArchive);
  }

  @Override
  public Map<String, TicketArchive> findTicketArchivesByTicketIdAndVersionMap(
      Map<String, Integer> ticketIdAndVersionMap) {
    Iterable<Ticket> tickets = ticketRepository.findAllById(ticketIdAndVersionMap.keySet());
    Map<String, TicketArchive> ticketArchives = new HashMap<>();
    for (Ticket ticket : tickets) {
      TicketArchive ticketArchive = getTicketArchiveFromTicketByVersion(ticket,
          ticketIdAndVersionMap.get(ticket.getId()));
      ticketArchives.put(ticket.getId(), ticketArchive);
    }
    return ticketArchives;
  }

  private TicketArchive getTicketArchiveFromTicketByVersion(Ticket ticket, Integer version) {
    for (TicketArchive ticketArchive : ticket.getTicketArchives()) {
      if (ticketArchive.getVersion().equals(version)) {
        return ticketArchive;
      }
    }
    return null;
  }
}
