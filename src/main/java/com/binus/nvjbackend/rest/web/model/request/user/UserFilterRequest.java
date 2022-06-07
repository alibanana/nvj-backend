package com.binus.nvjbackend.rest.web.model.request.user;

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
public class UserFilterRequest implements Serializable {

  private static final long serialVersionUID = -3182942450936761433L;

  private String id;
  private String firstname;
  private String lastname;
  private String username;
  private String email;
  private String phoneNumber;
  private String placeOfBirth;
  private String roleType;
}
