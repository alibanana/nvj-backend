package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

  User getCurrentUserDetails(String token);
}
