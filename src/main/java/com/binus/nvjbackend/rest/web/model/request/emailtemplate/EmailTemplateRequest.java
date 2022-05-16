package com.binus.nvjbackend.rest.web.model.request.emailtemplate;

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
public class EmailTemplateRequest implements Serializable {

  private static final long serialVersionUID = 5768903366994702123L;

  @NotBlank
  private String templateName;

  @NotBlank
  private String from;

  @NotBlank
  private String subject;

  @NotBlank
  private String content;
}
