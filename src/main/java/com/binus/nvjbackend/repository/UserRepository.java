package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

  User findByUsername(String username);

  Boolean existsByFirstnameAndLastname(String firstname, String lastname);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
