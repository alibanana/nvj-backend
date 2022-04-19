package com.binus.nvjbackend.rest.web.model.request.systemparameter;

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
public class SystemParameterRequest implements Serializable {

  private static final long serialVersionUID = -509786292783386753L;

  @NotBlank
  private String key;

  @NotBlank
  private String value;

  @NotBlank
  private String description;

  @NotBlank
  private String type;
}
