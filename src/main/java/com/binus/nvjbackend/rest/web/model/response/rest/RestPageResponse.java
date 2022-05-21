package com.binus.nvjbackend.rest.web.model.response.rest;

import com.binus.nvjbackend.rest.web.model.response.error.ErrorFieldAndMessageResponse;
import com.binus.nvjbackend.rest.web.model.response.error.ErrorResponse;
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
public class RestPageResponse<T> extends RestBaseResponse implements Serializable {

  private static final long serialVersionUID = -2909373735367131873L;

  private List<T> content;
  private PageMetaData pageMetaData;

  public RestPageResponse(RestBaseResponse baseResponse) {
    setErrorCode(baseResponse.getErrorCode());
    setErrorMessage(baseResponse.getErrorMessage());
    setSuccess(baseResponse.isSuccess());
  }
}
