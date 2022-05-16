package com.binus.nvjbackend.rest.web.model.request.emailtemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailTemplateSendRequest implements Serializable {

  private static final long serialVersionUID = 6764542998340361954L;

  @NotBlank
  String to;

  @NotBlank
  String templateName;

  @NotNull
  Map<String, Object> templateKeyAndValues;
}
