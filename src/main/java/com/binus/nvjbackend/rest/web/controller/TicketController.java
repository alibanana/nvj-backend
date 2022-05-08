package com.binus.nvjbackend.rest.web.controller;

import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.request.ticket.TicketRequest;
import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.service.TicketService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(value = "Tickets", description = "Tickets Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_TICKET)
public class TicketController extends BaseController {

  @Autowired
  private TicketService ticketService;

  @PostMapping(value = ApiPath.TICKET_CREATE)
  public RestBaseResponse create(@Valid @RequestBody TicketRequest request) {
    ticketService.create(request);
    return toBaseResponse();
  }
}
