package com.binus.nvjbackend.rest.web.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse implements Serializable {

  private static final long serialVersionUID = -2519107299536565448L;

  private String id;
  private String firstname;
  private String lastname;
  private String username;
  private String email;
  private String token;
  private String phoneNumber;
  private String placeOfBirth;
  private Date dateOfBirth;
  private RoleResponse role;
  private String qrCodeImageUrl;
}
