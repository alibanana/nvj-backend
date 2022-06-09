package com.binus.nvjbackend.rest.web.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

  private static final long serialVersionUID = 1l;

  private String id;
  private String firstname;
  private String lastname;
  private String username;
  private String email;
  private String token;

  @JsonIgnore
  private String password;

  private String phoneNumber;
  private String placeOfBirth;
  private Date dateOfBirth;
  private String roleName;

  private Collection<? extends GrantedAuthority> authorities;

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
