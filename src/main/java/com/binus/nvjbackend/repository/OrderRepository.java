package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String>, OrderRepositoryCustom {

  boolean existsByMidtransOrderId(String midtransOrderId);

  Order findByMidtransOrderId(String midtransOrderId);
}
