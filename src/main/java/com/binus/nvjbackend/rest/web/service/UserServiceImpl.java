package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.User;
import com.binus.nvjbackend.repository.UserRepository;
import com.binus.nvjbackend.rest.web.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserRepository userRepository;

  @Override
  public User getCurrentUserDetails(String token) {
    String username = jwtUtil.getUsernameFromJwtToken(token);
    return userRepository.findByUsername(username);
  }
}
