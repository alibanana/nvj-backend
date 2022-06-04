package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.OnSiteExperience;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OnSiteExperienceRepository extends MongoRepository<OnSiteExperience, String>,
    OnSiteExperienceRepositoryCustom {

  boolean existsByTitle(String title);
}
