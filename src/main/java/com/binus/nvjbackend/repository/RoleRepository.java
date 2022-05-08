package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

  boolean existsByNameAndRoleType(String name, String roleType);

  Role findByName(String name);

  Role findByRoleType(String roleType);

  boolean existsByName(String name);

  void deleteByName(String name);
}
