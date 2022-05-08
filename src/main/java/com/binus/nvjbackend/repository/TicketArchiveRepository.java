package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.TicketArchive;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketArchiveRepository extends MongoRepository<TicketArchive, String> {
}
