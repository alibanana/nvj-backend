package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface TicketRepositoryCustom {

  Page<Ticket> findAllByTitleAndPriceBetween(String title, Integer fromPrice, Integer toPrice,
      PageRequest pageRequest);
}
