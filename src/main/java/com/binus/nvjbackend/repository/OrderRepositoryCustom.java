package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

public interface OrderRepositoryCustom {

  Page<Order> findAllByFilter(
      String id, String firstname, String lastname, String email, String phoneNumber,
      Date fromVisitDate, Date toVisitDate, String paymentType, Boolean isManualOrder,
      String midtransOrderId, String midtransTransactionStatus, PageRequest pageRequest);

  List<Order> findOrderByCreationDate(Date fromCreateDate, Date toCreateDate);
}
