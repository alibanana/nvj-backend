package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepository extends MongoRepository<Image, String> {

  void deleteByName(String name);
}
