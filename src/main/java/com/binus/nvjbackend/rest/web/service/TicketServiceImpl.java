package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Ticket;
import com.binus.nvjbackend.model.entity.TicketArchive;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.TicketRepository;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketRequest;
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

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

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
  public Page<Ticket> findByFilter(Integer page, Integer size, String orderBy, String sortBy,
      TicketFilterRequest request) {
    PageRequest pageRequest = validateAndGetPageRequest(page, size, orderBy, sortBy);
    return ticketRepository.findAllByTitleAndPriceBetweenAndPurchasableEquals(request.getTitle(),
        request.getFromPrice(), request.getToPrice(), request.getPurchasable(), pageRequest);
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

  private PageRequest validateAndGetPageRequest(Integer page, Integer size, String orderBy,
      String sortBy) {
    page = validateAndInitializePageNumber(page);
    size = validateAndInitializePageSize(size);
    validateSortBy(sortBy);
    return getPageRequest(page, size, orderBy, sortBy);
  }

  private int validateAndInitializePageNumber(Integer page) {
    if (Objects.nonNull(page) && page < 0) {
      throw new BaseException(ErrorCode.PAGE_NUMBER_LESS_THAN_ZERO);
    }
    return Objects.isNull(page) ? 0 : page;
  }

  private int validateAndInitializePageSize(Integer size) {
    if (Objects.nonNull(size) && size <= 0) {
      throw new BaseException(ErrorCode.PAGE_SIZE_LESS_THAN_OR_EQUAL_TO_ZERO);
    }
    return Objects.isNull(size) ? 10 : size;
  }

  private void validateSortBy(String sortBy) {
    if (Objects.nonNull(sortBy) && !sortBy.equals(ASC.name()) && !sortBy.equals(DESC.name())) {
      throw new BaseException(ErrorCode.SORT_BY_VALUES_INVALID);
    }
  }

  private PageRequest getPageRequest(Integer page, Integer size, String orderBy, String sortBy) {
    if (Objects.isNull(orderBy) && Objects.isNull(sortBy)) {
      return PageRequest.of(page, size);
    } else {
      return PageRequest.of(page, size, Sort.Direction.fromString(sortBy), orderBy);
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
    ticket.setPurchasable(request.isPurchasable());
    TicketArchive ticketArchive = ticketArchiveService.createAndReturnTicketArchive(request,
        ticket.getVersion() + 1);
    ticket.getTicketArchives().add(ticketArchive);
    return ticketRepository.save(ticket);
  }
}
