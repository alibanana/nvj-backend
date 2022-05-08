package com.binus.nvjbackend.rest.web.model.request.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleRequest {

  @NotBlank
  private String name;

  @NotBlank
  private String roleType;

  @NotBlank
  private String description;
}
