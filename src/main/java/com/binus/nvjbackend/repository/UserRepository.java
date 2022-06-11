package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

  User findByUsername(String username);

  Boolean existsByFirstnameAndLastname(String firstname, String lastname);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  Boolean existsByFirstname(String firstname);

  Boolean existsByLastname(String lastname);
}
