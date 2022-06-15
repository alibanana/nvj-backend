package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.OnSiteExperience;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceAddImageRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceUpdateRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceUpdateThumbnailRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OnSiteExperienceService {

  OnSiteExperience create(OnSiteExperienceRequest request);

  Page<OnSiteExperience> findByFilter(Integer page, Integer size, String orderBy, String sortBy,
      OnSiteExperienceFilterRequest request);

  List<OnSiteExperience> findAll();

  List<OnSiteExperience> findAllWithSorting(String orderBy, String sortBy);

  OnSiteExperience findById(String id);

  OnSiteExperience updateById(String id, OnSiteExperienceUpdateRequest request);

  OnSiteExperience updateThumbnailById(String id, OnSiteExperienceUpdateThumbnailRequest request);

  OnSiteExperience removeImageByIdAndImageId(String id, String imageId);

  OnSiteExperience addImageById(String id, OnSiteExperienceAddImageRequest request);

  void deleteById(String id);
}
