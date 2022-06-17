package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.Order;
import com.binus.nvjbackend.model.enums.MongoFieldNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Page<Order> findAllByFilter(String id, String firstname, String lastname, String email,
      String phoneNumber, Date fromVisitDate, Date toVisitDate, String paymentType,
      Boolean isManualOrder, String midtransOrderId, String midtransTransactionStatus,
      PageRequest pageRequest) {
    Query query = new Query();

    if (Objects.nonNull(id) && StringUtils.hasText(id)) {
      query.addCriteria(where(MongoFieldNames.ORDER_ID)
          .is(id));
    }

    if (Objects.nonNull(firstname) && StringUtils.hasText(firstname)) {
      query.addCriteria(where(MongoFieldNames.ORDER_FIRSTNAME)
          .regex(String.format(".*%s.*", firstname), "i"));
    }

    if (Objects.nonNull(lastname) && StringUtils.hasText(lastname)) {
      query.addCriteria(where(MongoFieldNames.ORDER_LASTNAME)
          .regex(String.format(".*%s.*", lastname), "i"));
    }

    if (Objects.nonNull(email) && StringUtils.hasText(email)) {
      query.addCriteria(where(MongoFieldNames.ORDER_EMAIL)
          .regex(String.format(".*%s.*", email), "i"));
    }

    if (Objects.nonNull(phoneNumber) && StringUtils.hasText(phoneNumber)) {
      query.addCriteria(where(MongoFieldNames.ORDER_PHONE_NUMBER)
          .regex(String.format(".*%s.*", phoneNumber), "i"));
    }

    if (Objects.nonNull(fromVisitDate) && Objects.isNull(toVisitDate)) {
      query.addCriteria(where(MongoFieldNames.ORDER_VISIT_DATE)
          .gte(fromVisitDate));
    }

    if (Objects.nonNull(toVisitDate) && Objects.isNull(fromVisitDate)) {
      query.addCriteria(where(MongoFieldNames.ORDER_VISIT_DATE)
          .lte(toVisitDate));
    }

    if (Objects.nonNull(fromVisitDate) && Objects.nonNull(toVisitDate)) {
      query.addCriteria(where(MongoFieldNames.ORDER_VISIT_DATE)
          .gte(fromVisitDate).lte(toVisitDate));
    }

    if (Objects.nonNull(paymentType) && StringUtils.hasText(paymentType)) {
      query.addCriteria(where(MongoFieldNames.ORDER_PAYMENT_TYPE)
          .regex(String.format(".*%s.*", paymentType), "i"));
    }

    if (Objects.nonNull(isManualOrder)) {
      query.addCriteria(where(MongoFieldNames.ORDER_IS_MANUAL_ORDER)
          .is(isManualOrder));
    }

    if (Objects.nonNull(midtransOrderId) && StringUtils.hasText(midtransOrderId)) {
      query.addCriteria(where(MongoFieldNames.ORDER_MIDTRANS_ORDER_ID)
          .is(midtransOrderId));
    }

    if (Objects.nonNull(midtransTransactionStatus) && StringUtils.hasText(midtransTransactionStatus)) {
      query.addCriteria(where(MongoFieldNames.ORDER_MIDTRANS_TRANSACTION_STATUS)
          .is(midtransTransactionStatus));
    }

    query.with(pageRequest);
    List<Order> orderList = mongoTemplate.find(query, Order.class);
    return PageableExecutionUtils.getPage(orderList, pageRequest,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Order.class));
  }

  @Override
  public List<Order> findOrderByCreationDate(Date fromCreateDate, Date toCreateDate) {
    Query query = new Query();

    query.fields()
        .include(MongoFieldNames.ID)
        .include(MongoFieldNames.ORDER_TOTAL_PRICE)
        .include(MongoFieldNames.ORDER_ORDER_ITEMS);

    if (Objects.nonNull(toCreateDate) && Objects.isNull(fromCreateDate)) {
      query.addCriteria(where(MongoFieldNames.CREATED_AT)
          .lte(toCreateDate));
    }

    if (Objects.nonNull(fromCreateDate) && Objects.nonNull(toCreateDate)) {
      query.addCriteria(where(MongoFieldNames.CREATED_AT)
          .gte(fromCreateDate).lte(toCreateDate));
    }

    return mongoTemplate.find(query, Order.class);
  }
}
