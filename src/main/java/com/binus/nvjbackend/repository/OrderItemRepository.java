package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.OrderItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderItemRepository extends MongoRepository<OrderItem, String> {
}
