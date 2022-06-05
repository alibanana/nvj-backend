package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.Ticket;
import com.binus.nvjbackend.model.enums.MongoFieldNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class TicketRepositoryImpl implements TicketRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Page<Ticket> findAllByIdAndTitleAndPriceBetweenAndPurchasableEqualsAndMarkForDeleteEquals(String id,
      String title, Integer fromPrice, Integer toPrice, Boolean purchasable, Boolean markForDelete,
      PageRequest pageRequest) {
    Query query = new Query();

    if (Objects.nonNull(id) && StringUtils.hasText(id)) {
      query.addCriteria(where(MongoFieldNames.TICKET_ID)
          .is(id));
    }

    if (Objects.nonNull(title)) {
      query.addCriteria(where(MongoFieldNames.TICKET_TITLE)
          .regex(String.format(".*%s.*", title), "i"));
    }

    if (Objects.nonNull(fromPrice) && Objects.nonNull(toPrice)) {
      query.addCriteria(where(MongoFieldNames.TICKET_PRICE)
          .gte(fromPrice).lte(toPrice));
    }

    if (Objects.nonNull(purchasable)) {
      query.addCriteria(where(MongoFieldNames.TICKET_PURCHASABLE)
          .is(purchasable));
    }

    if (Objects.nonNull(markForDelete)) {
      query.addCriteria(where(MongoFieldNames.TICKET_MARK_FOR_DELETE)
          .is(markForDelete));
    }

    query.with(pageRequest);
    List<Ticket> ticketList = mongoTemplate.find(query, Ticket.class);
    return PageableExecutionUtils.getPage(ticketList, pageRequest,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Ticket.class));
  }

  @Override
  public List<Ticket> findAllWithSortingAndMarkForDeleteFalse(Sort sort) {
    Query query = new Query();
    query.addCriteria(where(MongoFieldNames.TICKET_MARK_FOR_DELETE)
        .is(Boolean.FALSE));
    query.with(sort);
    return mongoTemplate.find(query, Ticket.class);
  }
}
