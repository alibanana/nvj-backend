package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface OrderRepositoryCustom {

  Page<Order> findAllByIdAndFirstnameAndLastnameAndEmailAndPhoneNumberAndPaymentTypeAndManualOrderEqualsAndMidtransOrderIdAndMidtransTransactionStatus(
      String id, String firstname, String lastname, String email, String phoneNumber,
      String paymentType, Boolean isManualOrder, String midtransOrderId,
      String midtransTransactionStatus, PageRequest pageRequest);
}
