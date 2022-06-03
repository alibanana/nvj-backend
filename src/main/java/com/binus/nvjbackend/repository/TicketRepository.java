package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<Ticket, String>, TicketRepositoryCustom {

  boolean existsByTitle(String title);
}
