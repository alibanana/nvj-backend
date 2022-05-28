package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface TicketRepositoryCustom {

  Page<Ticket> findAllByTitleAndPriceBetweenAndPurchasableEquals(String title, Integer fromPrice,
      Integer toPrice, Boolean purchasable, PageRequest pageRequest);
}
