package com.binus.nvjbackend.rest.web.model.request.authentication;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterRequest implements Serializable {

  private static final long serialVersionUID = 4351953499150532640L;

  @NotBlank
  private String fullname;

  @NotBlank
  private String username;

  @NotBlank
  private String email;

  @NotBlank
  private String password;

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
