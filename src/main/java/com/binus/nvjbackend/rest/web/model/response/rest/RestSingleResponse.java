package com.binus.nvjbackend.rest.web.model.response.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class RestSingleResponse<T> extends RestBaseResponse implements Serializable {

  private static final long serialVersionUID = -5818316705062650217L;

  private T content;

  public RestSingleResponse(RestBaseResponse baseResponse) {
    setErrorCode(baseResponse.getErrorCode());
    setErrorMessage(baseResponse.getErrorMessage());
    setSuccess(baseResponse.isSuccess());
  }
}
