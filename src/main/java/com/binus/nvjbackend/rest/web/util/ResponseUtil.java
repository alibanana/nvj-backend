package com.binus.nvjbackend.rest.web.util;

import com.binus.nvjbackend.model.dto.ErrorDto;
import com.binus.nvjbackend.model.dto.ErrorFieldAndMessageDto;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.model.response.error.ErrorFieldAndMessageResponse;
import com.binus.nvjbackend.rest.web.model.response.error.ErrorResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ResponseUtil {

  public ResponseEntity<RestBaseResponse> buildErrorResponse(BaseException e) {
    return buildErrorResponse(e.getCode(), e.getMessage(), e.getHttpStatus(), e.getErrorList(), null);
  }

  public ResponseEntity<RestBaseResponse> buildErrorResponse(String errCode, String errMsg,
      int httpStatus, List<ErrorDto> errorList,
      List<ErrorFieldAndMessageDto> errorFieldAndMessageList) {
    RestBaseResponse baseResponse = RestBaseResponse.parentBuilder()
        .errorCode(errCode)
        .errorMessage(errMsg)
        .success(false)
        .errorList(buildErrorResponseList(errorList))
        .errorFieldList(buildErrorFieldAndMessageResponseList(errorFieldAndMessageList))
        .build();
    return ResponseEntity.status(httpStatus)
        .body(baseResponse);
  }

  private List<ErrorResponse> buildErrorResponseList(List<ErrorDto> errorList) {
    return Optional.ofNullable(errorList)
        .filter(CollectionUtils::isNotEmpty)
        .map(errorDtoList -> errorDtoList.stream().map(
                errorDto -> ErrorResponse.builder()
                    .message(errorDto.getErrorMessage())
                    .code(errorDto.getErrorCode())
                    .build())
            .collect(Collectors.toList()))
        .orElse(null);
  }

  private List<ErrorFieldAndMessageResponse> buildErrorFieldAndMessageResponseList(
      List<ErrorFieldAndMessageDto> errorFieldAndMessageList) {
    return Optional.ofNullable(errorFieldAndMessageList)
        .filter(CollectionUtils::isNotEmpty)
        .map(errorFieldAndMessageDtoList -> errorFieldAndMessageDtoList.stream().map(
            errorFieldAndMessageDto -> ErrorFieldAndMessageResponse.builder()
                .relatedField(errorFieldAndMessageDto.getRelatedField())
                .message(errorFieldAndMessageDto.getErrorMessage())
                .build())
            .collect(Collectors.toList()))
        .orElse(null);
  }
}
