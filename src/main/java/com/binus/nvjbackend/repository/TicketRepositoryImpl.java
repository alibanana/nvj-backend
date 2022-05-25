package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.Ticket;
import com.binus.nvjbackend.model.enums.MongoFieldNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class TicketRepositoryImpl implements TicketRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Page<Ticket> findAllByTitleAndPriceBetween(String title, Integer fromPrice,
      Integer toPrice, PageRequest pageRequest) {
    Query query = new Query();

    if (Objects.nonNull(fromPrice) && Objects.nonNull(toPrice)) {
      query.addCriteria(where(MongoFieldNames.TICKET_PRICE)
          .gte(fromPrice).lte(toPrice));
    }

    if (Objects.nonNull(title)) {
      query.addCriteria(where(MongoFieldNames.TICKET_TITLE)
          .regex(String.format(".*%s.*", title), "i"));
    }

    query.with(pageRequest);
    List<Ticket> ticketList = mongoTemplate.find(query, Ticket.class);
    return PageableExecutionUtils.getPage(ticketList, pageRequest,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Ticket.class));
  }
}
