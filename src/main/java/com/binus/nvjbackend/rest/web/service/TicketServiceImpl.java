package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Ticket;
import com.binus.nvjbackend.model.entity.TicketArchive;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.TicketRepository;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketRequest;
import com.binus.nvjbackend.rest.web.util.OtherUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

  @Autowired
  private TicketArchiveService ticketArchiveService;

  @Autowired
  private TicketRepository ticketRepository;

  @Autowired
  private OtherUtil otherUtil;

  @Override
  public Ticket create(TicketRequest request) {
    validateTicketDoesNotExistsByTitle(request.getTitle());
    otherUtil.validatePhoneNumber(request.getPhoneNumber());
    TicketArchive ticketArchive = ticketArchiveService.createAndReturnTicketArchive(request, 0);
    return ticketRepository.save(initializeNewTicket(ticketArchive));
  }

  @Override
  public List<Ticket> findAllWithSorting(String orderBy, String sortBy) {
    if (Objects.nonNull(orderBy) || Objects.nonNull(sortBy)) {
      otherUtil.validateSortByAndOrderBy(sortBy, orderBy);
      return ticketRepository.findAllWithSortingAndMarkForDeleteFalse(Sort.by(sortBy, orderBy));
    }
    return ticketRepository.findAllByMarkForDeleteFalse();
  }

  @Override
  public Page<Ticket> findByFilter(Integer page, Integer size, String orderBy, String sortBy,
      TicketFilterRequest request, Boolean isClientApi) {
    PageRequest pageRequest = otherUtil.validateAndGetPageRequest(page, size, orderBy, sortBy);
    setTicketFilterRequestMarkForDeleteValue(request, isClientApi);
    return ticketRepository.findAllByIdAndTitleAndPriceBetweenAndPurchasableEqualsAndMarkForDeleteEquals(
        request.getId(), request.getTitle(), request.getFromPrice(), request.getToPrice(),
        request.getPurchasable(), request.getMarkForDelete(), pageRequest);
  }

  @Override
  public List<Ticket> findByIds(List<String> ids) {
    List<Ticket> tickets = new ArrayList<>();
    ticketRepository.findAllById(ids).forEach(tickets::add);
    if (tickets.isEmpty()) {
      throw new BaseException(ErrorCode.TICKET_NOT_FOUND);
    }
    return tickets;
  }

  @Override
  public Ticket updateById(String id, TicketRequest request) {
    Ticket ticket = Optional.of(ticketRepository.findById(id)).get()
        .orElse(null);
    if (Objects.isNull(ticket)) {
      throw new BaseException(ErrorCode.TICKET_NOT_FOUND);
    }
    validateNewTicketTitleUnique(ticket.getTitle(), request.getTitle());
    return updateTicketAndTicketArchives(ticket, request);
  }

  @Override
  public void deleteById(String id) {
    Ticket ticket = Optional.of(ticketRepository.findById(id)).get()
        .orElse(null);
    if (Objects.isNull(ticket)) {
      throw new BaseException(ErrorCode.TICKET_NOT_FOUND);
    }
    ticket.setMarkForDelete(true);
    ticketRepository.save(ticket);
  }

  @Override
  public void validateTicketsPurchasable(List<Ticket> tickets) {
    for (Ticket ticket : tickets) {
      if (!ticket.getPurchasable()) {
        throw new BaseException(ErrorCode.TICKETS_ARE_NOT_PURCHASABLE);
      }
    }
  }

  private void validateTicketDoesNotExistsByTitle(String title) {
    if (ticketRepository.existsByTitle(title)) {
      throw new BaseException(ErrorCode.TICKET_ALREADY_EXISTS);
    }
  }

  private void setTicketFilterRequestMarkForDeleteValue(TicketFilterRequest request,
      Boolean isClientApi) {
    if (isClientApi) {
      request.setMarkForDelete(false);
    } else if (Objects.isNull(request.getMarkForDelete())) {
      request.setMarkForDelete(false);
    }
  }

  private Ticket initializeNewTicket(TicketArchive ticketArchive) {
    Ticket ticket = new Ticket();
    BeanUtils.copyProperties(ticketArchive, ticket);
    ticket.setVersion(null); // let version be handled by springboot.
    ticket.setTicketArchives(Collections.singletonList(ticketArchive));
    return ticket;
  }

  private void validateNewTicketTitleUnique(String currentTitle, String newTitle) {
    if (!currentTitle.equals(newTitle) && ticketRepository.existsByTitle(newTitle)) {
      throw new BaseException(ErrorCode.NEW_TICKET_TITLE_EXISTS);
    }
  }

  private Ticket updateTicketAndTicketArchives(Ticket ticket, TicketRequest request) {
    ticket.setTitle(request.getTitle());
    ticket.setDescription(request.getDescription());
    ticket.setPrice(request.getPrice());
    ticket.setPhoneNumber(request.getPhoneNumber());
    ticket.setContactName(request.getContactName());
    ticket.setPurchasable(request.getPurchasable());
    TicketArchive ticketArchive = ticketArchiveService.createAndReturnTicketArchive(request,
        ticket.getVersion() + 1);
    ticket.getTicketArchives().add(ticketArchive);
    return ticketRepository.save(ticket);
  }
}
