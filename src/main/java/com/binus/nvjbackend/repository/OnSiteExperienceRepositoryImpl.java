package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.OnSiteExperience;
import com.binus.nvjbackend.model.enums.MongoFieldNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class OnSiteExperienceRepositoryImpl implements OnSiteExperienceRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;


  @Override
  public Page<OnSiteExperience> findAllByIdAndTitle(String id, String title,
      PageRequest pageRequest) {
    Query query = new Query();

    if (Objects.nonNull(id) && StringUtils.hasText(id)) {
      query.addCriteria(where(MongoFieldNames.EXPERIENCE_ID)
          .is(id));
    }

    if (Objects.nonNull(title)) {
      query.addCriteria(where(MongoFieldNames.EXPERIENCE_TITLE)
          .regex(String.format(".*%s.*", title), "i"));
    }

    query.with(pageRequest);
    List<OnSiteExperience> onSiteExperienceList = mongoTemplate.find(query, OnSiteExperience.class);
    return PageableExecutionUtils.getPage(onSiteExperienceList, pageRequest,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), OnSiteExperience.class));
  }
}
