package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.OnSiteExperience;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OnSiteExperienceService {

  OnSiteExperience create(OnSiteExperienceRequest request);

  Page<OnSiteExperience> findByFilter(Integer page, Integer size, String orderBy, String sortBy,
      OnSiteExperienceFilterRequest request);

  List<OnSiteExperience> findAll();

  OnSiteExperience findById(String id);
}
