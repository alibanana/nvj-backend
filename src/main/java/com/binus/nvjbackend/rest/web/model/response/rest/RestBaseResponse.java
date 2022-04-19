package com.binus.nvjbackend.rest.web.model.response.rest;

import com.binus.nvjbackend.rest.web.model.response.error.ErrorFieldAndMessageResponse;
import com.binus.nvjbackend.rest.web.model.response.error.ErrorResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Data
@SuperBuilder(builderMethodName = "parentBuilder")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestBaseResponse implements Serializable {

  private static final long serialVersionUID = -6286302116783067338L;

  private String errorMessage;
  private String errorCode;
  private boolean success;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<ErrorResponse> errorList;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<ErrorFieldAndMessageResponse> errorFieldList;
}
