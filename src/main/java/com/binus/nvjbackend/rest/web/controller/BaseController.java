package com.binus.nvjbackend.rest.web.controller;

import com.binus.nvjbackend.rest.web.model.response.rest.PageMetaData;
import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestListResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestPageResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import org.springframework.data.domain.Page;

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

  protected <T> RestPageResponse<T> toPageResponse(List<T> data, Page<?> page) {
    return RestPageResponse.<T>builder()
        .success(true)
        .content(data)
        .pageMetaData(PageMetaData.builder()
            .pageSize(page.getSize())
            .pageNumber(page.getNumber())
            .totalRecords(page.getTotalElements())
            .build())
        .build();
  }
}
