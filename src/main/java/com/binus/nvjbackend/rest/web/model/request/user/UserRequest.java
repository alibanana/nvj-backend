package com.binus.nvjbackend.rest.web.model.request.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest implements Serializable {

  private static final long serialVersionUID = 6619937784753761059L;

  @NotBlank
  private String firstname;

  @NotBlank
  private String lastname;

  @NotBlank
  private String username;

  @NotBlank
  private String email;

  @NotBlank
  private String phoneNumber;

  @NotBlank
  private String placeOfBirth;

  @NotNull
  @JsonFormat(pattern = "dd-MM-yyyy")
  private Date dateOfBirth;

  @NotBlank
  private String roleType;
}
