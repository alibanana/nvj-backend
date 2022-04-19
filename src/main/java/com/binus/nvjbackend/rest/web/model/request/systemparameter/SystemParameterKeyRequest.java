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
public class SystemParameterKeyRequest implements Serializable {

  private static final long serialVersionUID = 7290044155315394173L;

  @NotBlank
  private String key;
}
