package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.OnSiteExperience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface OnSiteExperienceRepositoryCustom {

  Page<OnSiteExperience> findAllByIdAndTitle(String id, String title, PageRequest pageRequest);

  List<OnSiteExperience> findAllWithSorting(Sort sort);
}
