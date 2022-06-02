package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.User;
import com.binus.nvjbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    return UserDetailsImpl.builder()
        .id(user.getId())
        .firstname(user.getFirstname())
        .lastname(user.getLastname())
        .username(user.getUsername())
        .email(user.getEmail())
        .password(user.getPassword())
        .authorities(getUserAuthorities(user))
        .phoneNumber(user.getPhoneNumber())
        .placeOfBirth(user.getPlaceOfBirth())
        .dateOfBirth(user.getDateOfBirth())
        .build();
  }

  private List<SimpleGrantedAuthority> getUserAuthorities(User user) {
    if (Objects.isNull(user.getRole())) return null;
    return Collections.singletonList(
        new SimpleGrantedAuthority(user.getRole().getRoleType().name()));
  }
}
