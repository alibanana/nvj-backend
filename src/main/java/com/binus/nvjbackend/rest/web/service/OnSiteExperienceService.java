package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.OnSiteExperience;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceRequest;

import java.util.List;

public interface OnSiteExperienceService {

  OnSiteExperience create(OnSiteExperienceRequest request);

  List<OnSiteExperience> findAll();

  OnSiteExperience findById(String id);
}
