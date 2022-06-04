package com.binus.nvjbackend.rest.web.controller.server;

import com.binus.nvjbackend.model.entity.Ticket;
import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketRequest;
import com.binus.nvjbackend.rest.web.model.response.TicketResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestPageResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.TicketService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Tickets", description = "Tickets Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_TICKET)
@Validated
public class TicketController extends BaseController {

  @Autowired
  private TicketService ticketService;

  @PostMapping(value = ApiPath.TICKET_CREATE)
  public RestSingleResponse<TicketResponse> create(@Valid @RequestBody TicketRequest request) {
    Ticket ticket = ticketService.create(request);
    return toSingleResponse(toTicketResponse(ticket));
  }

  @PostMapping(value = ApiPath.TICKET_FIND_BY_FILTER)
  public RestPageResponse<TicketResponse> findByFilter(@RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size, @RequestParam(required = false) String orderBy,
      @RequestParam(required = false) String sortBy,
      @Valid @RequestBody TicketFilterRequest request) {
    Page<Ticket> tickets = ticketService.findByFilter(page, size, orderBy, sortBy, request, false);
    List<TicketResponse> content = tickets.getContent().stream()
        .map(this::toTicketResponse)
        .collect(Collectors.toList());
    return toPageResponse(content, tickets);
  }

  @PutMapping(value = ApiPath.TICKET_UPDATE_BY_ID)
  public RestSingleResponse<TicketResponse> updateById(@RequestParam String id,
      @Valid @RequestBody TicketRequest request) {
    Ticket ticket = ticketService.updateById(id, request);
    return toSingleResponse(toTicketResponse(ticket));
  }

  @DeleteMapping(value = ApiPath.TICKET_DELETE_BY_ID)
  public RestBaseResponse deleteById(@RequestParam String id) {
    ticketService.deleteById(id);
    return toBaseResponse();
  }

  private TicketResponse toTicketResponse(Ticket ticket) {
    return TicketResponse.builder()
        .id(ticket.getId())
        .title(ticket.getTitle())
        .description(ticket.getDescription())
        .price(ticket.getPrice())
        .phoneNumber(ticket.getPhoneNumber())
        .contactName(ticket.getContactName())
        .purchasable(ticket.getPurchasable())
        .markForDelete(ticket.getMarkForDelete())
        .build();
  }
}
