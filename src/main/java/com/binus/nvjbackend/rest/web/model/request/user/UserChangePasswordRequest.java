package com.binus.nvjbackend.rest.web.model.request.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserChangePasswordRequest implements Serializable {

  private static final long serialVersionUID = 3377820779651995144L;

  @NotBlank
  private String password;

  @NotBlank
  private String newPassword;

  @NotBlank
  private String confirmNewPassword;
}
