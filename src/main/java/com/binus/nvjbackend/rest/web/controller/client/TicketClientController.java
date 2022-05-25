package com.binus.nvjbackend.rest.web.controller.client;

import com.binus.nvjbackend.model.entity.Ticket;
import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPathClient;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketFilterRequest;
import com.binus.nvjbackend.rest.web.model.response.TicketClientResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestListResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestPageResponse;
import com.binus.nvjbackend.rest.web.service.TicketService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Client - Tickets", description = "Client - Tickets Service API")
@RestController
@RequestMapping(value = ApiPathClient.BASE_PATH_TICKET)
@Validated
public class TicketClientController extends BaseController {

  @Autowired
  private TicketService ticketService;

  @PostMapping(value = ApiPathClient.TICKET_FIND_BY_FILTER)
  public RestPageResponse<TicketClientResponse> findByFilter(@RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size, @RequestParam(required = false) String orderBy,
      @RequestParam(required = false) String sortBy,
      @Valid @RequestBody TicketFilterRequest request) {
    Page<Ticket> tickets = ticketService.findByFilter(page, size, orderBy, sortBy, request);
    List<TicketClientResponse> content = tickets.getContent().stream()
        .map(this::toTicketClientResponse)
        .collect(Collectors.toList());
    return toPageResponse(content, tickets);
  }

  @PostMapping(value = ApiPathClient.TICKET_FIND_BY_IDS)
  public RestListResponse<TicketClientResponse> findByIds(
      @RequestParam(value = "id") @NotEmpty List<String> ids) {
    List<Ticket> tickets = ticketService.findByIds(ids);
    return toListResponse(tickets.stream()
        .map(this::toTicketClientResponse)
        .collect(Collectors.toList()));
  }

  private TicketClientResponse toTicketClientResponse(Ticket ticket) {
    return TicketClientResponse.builder()
        .id(ticket.getId())
        .title(ticket.getTitle())
        .price(ticket.getPrice())
        .build();
  }
}
