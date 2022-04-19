package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.rest.web.model.request.authentication.LoginRequest;
import com.binus.nvjbackend.rest.web.model.request.authentication.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {

  UserDetails login(LoginRequest loginRequest);

  void register(RegisterRequest registerRequest);
}
