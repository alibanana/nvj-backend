package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;

public interface TicketRepositoryCustom {

  Page<Ticket> findAllByIdAndTitleAndPriceBetweenAndPurchasableEqualsAndMarkForDeleteEquals(
      String id, String title, Integer fromPrice, Integer toPrice, Boolean purchasable,
      Boolean markForDelete, PageRequest pageRequest);

  List<Ticket> findAllWithSortingAndMarkForDeleteFalse(Sort sort);
}
