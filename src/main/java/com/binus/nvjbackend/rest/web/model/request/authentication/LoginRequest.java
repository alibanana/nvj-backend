package com.binus.nvjbackend.rest.web.model.request.authentication;

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
public class LoginRequest implements Serializable {

  private static final long serialVersionUID = -3885664923858274766L;

  @NotBlank
  private String username;

  @NotBlank
  private String password;
}
