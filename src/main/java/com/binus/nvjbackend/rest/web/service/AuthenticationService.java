package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.rest.web.model.request.authentication.LoginRequest;
import com.binus.nvjbackend.rest.web.model.request.authentication.RegisterRequest;
import com.google.zxing.WriterException;
import freemarker.template.TemplateException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.mail.MessagingException;
import java.io.IOException;

public interface AuthenticationService {

  UserDetails login(LoginRequest loginRequest);

  void register(RegisterRequest registerRequest) throws IOException, WriterException;

  void passwordRecovery(String username) throws TemplateException, MessagingException, IOException;
}
