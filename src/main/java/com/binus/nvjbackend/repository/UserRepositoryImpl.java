package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.User;
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

public class UserRepositoryImpl implements UserRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Page<User> findAllByIdAndFirstnameAndLastnameAndUsernameAndEmailAndPhoneNumberAndPlaceOfBirth(
      String id, String firstname, String lastname, String username, String email,
      String phoneNumber, String placeOfBirth, String roleId, PageRequest pageRequest) {
    Query query = new Query();

    if (Objects.nonNull(id) && StringUtils.hasText(id)) {
      query.addCriteria(where(MongoFieldNames.USER_ID)
          .is(id));
    }

    if (Objects.nonNull(firstname) && StringUtils.hasText(firstname)) {
      query.addCriteria(where(MongoFieldNames.USER_FIRSTNAME)
          .regex(String.format(".*%s.*", firstname), "i"));
    }

    if (Objects.nonNull(lastname) && StringUtils.hasText(lastname)) {
      query.addCriteria(where(MongoFieldNames.USER_LASTNAME)
          .regex(String.format(".*%s.*", lastname), "i"));
    }

    if (Objects.nonNull(username) && StringUtils.hasText(username)) {
      query.addCriteria(where(MongoFieldNames.USER_USERNAME)
          .regex(String.format(".*%s.*", username), "i"));
    }

    if (Objects.nonNull(email) && StringUtils.hasText(email)) {
      query.addCriteria(where(MongoFieldNames.USER_EMAIL)
          .regex(String.format(".*%s.*", email), "i"));
    }

    if (Objects.nonNull(phoneNumber) && StringUtils.hasText(phoneNumber)) {
      query.addCriteria(where(MongoFieldNames.USER_PHONE_NUMBER)
          .regex(String.format(".*%s.*", phoneNumber), "i"));
    }

    if (Objects.nonNull(placeOfBirth) && StringUtils.hasText(placeOfBirth)) {
      query.addCriteria(where(MongoFieldNames.USER_PLACE_OF_BIRTH)
          .regex(String.format(".*%s.*", placeOfBirth), "i"));
    }

    if (Objects.nonNull(roleId) && StringUtils.hasText(roleId)) {
      query.addCriteria(where(MongoFieldNames.USER_ROLE_TYPE)
          .is(roleId));
    }

    query.with(pageRequest);
    List<User> users = mongoTemplate.find(query, User.class);
    return PageableExecutionUtils.getPage(users, pageRequest,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), User.class));
  }
}
