package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UserRepositoryCustom {

  Page<User> findAllByIdAndFirstnameAndLastnameAndUsernameAndEmailAndPhoneNumberAndPlaceOfBirth(
      String id, String firstname, String lastname, String username, String email,
      String phoneNumber, String placeOfBirth, String roleId, PageRequest pageRequest);
}
