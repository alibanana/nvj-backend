package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.User;
import com.binus.nvjbackend.rest.web.model.request.user.UserChangePasswordRequest;
import com.binus.nvjbackend.rest.web.model.request.user.UserFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.user.UserRequest;
import com.google.zxing.WriterException;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface UserService {

  Page<User> findByFilter(Integer page, Integer size, String orderBy, String sortBy,
      UserFilterRequest request);

  List<User> findAll();

  User findById(String id);

  User updateById(String id, UserRequest request) throws IOException, WriterException;

  void deleteById(String id);

  User getCurrentUserDetails(String token);

  User changePassword(String token, UserChangePasswordRequest request);
}
