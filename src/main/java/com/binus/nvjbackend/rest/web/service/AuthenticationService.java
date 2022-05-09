package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.rest.web.model.request.authentication.LoginRequest;
import com.binus.nvjbackend.rest.web.model.request.authentication.RegisterRequest;
import com.google.zxing.WriterException;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

public interface AuthenticationService {

  UserDetails login(LoginRequest loginRequest);

  void register(RegisterRequest registerRequest) throws IOException, WriterException;
}
