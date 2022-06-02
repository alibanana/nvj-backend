package com.binus.nvjbackend.rest.web.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse implements Serializable {

  private static final long serialVersionUID = -798469217763610019L;

  private String id;
  private String firstname;
  private String lastname;
  private String username;
  private String email;
  private String token;
  private String phoneNumber;
  private String placeOfBirth;
  private String dateOfBirth;
}
