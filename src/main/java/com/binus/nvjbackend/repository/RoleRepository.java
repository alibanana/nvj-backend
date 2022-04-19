package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
}
