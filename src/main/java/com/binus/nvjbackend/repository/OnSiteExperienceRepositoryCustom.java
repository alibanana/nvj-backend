package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.OnSiteExperience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface OnSiteExperienceRepositoryCustom {

  Page<OnSiteExperience> findAllByIdAndTitle(String id, String title, PageRequest pageRequest);
}
