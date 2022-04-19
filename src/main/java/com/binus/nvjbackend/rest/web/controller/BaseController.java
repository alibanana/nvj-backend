package com.binus.nvjbackend.rest.web.controller;

import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestListResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;

import java.util.List;

public class BaseController {

  protected RestBaseResponse toBaseResponse() {
    return RestBaseResponse.parentBuilder()
        .success(true)
        .build();
  }

  protected <T> RestSingleResponse<T> toSingleResponse(T data) {
    return RestSingleResponse.<T>builder()
        .success(true)
        .content(data)
        .build();
  }

  protected <T> RestListResponse<T> toListResponse(List<T> data) {
    return RestListResponse.<T>builder()
        .success(true)
        .content(data)
        .build();
  }
}
