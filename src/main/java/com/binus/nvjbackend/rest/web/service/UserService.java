package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.User;
import com.binus.nvjbackend.rest.web.model.request.user.UserChangePasswordRequest;
import com.binus.nvjbackend.rest.web.model.request.user.UserFilterRequest;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

  Page<User> findByFilter(Integer page, Integer size, String orderBy, String sortBy,
      UserFilterRequest request);

  User getCurrentUserDetails(String token);

  User changePassword(String token, UserChangePasswordRequest request);
}
