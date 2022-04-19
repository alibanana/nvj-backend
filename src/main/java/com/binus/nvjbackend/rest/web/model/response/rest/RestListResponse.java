package com.binus.nvjbackend.rest.web.model.response.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Data
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class RestListResponse<T> extends RestBaseResponse implements Serializable {

  private static final long serialVersionUID = 2488577760142838781L;

  private List<T> content;

  public RestListResponse(RestBaseResponse baseResponse) {
    setErrorCode(baseResponse.getErrorCode());
    setErrorMessage(baseResponse.getErrorMessage());
    setSuccess(baseResponse.isSuccess());
  }
}
