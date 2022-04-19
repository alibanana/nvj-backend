package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.SystemParameter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SystemParameterRepository extends MongoRepository<SystemParameter, String> {

  Boolean existsByKey(String key);

  SystemParameter findByKey(String key);

  void deleteByKey(String key);
}
