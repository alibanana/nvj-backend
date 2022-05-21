package com.binus.nvjbackend.rest.web.controller.server;

import com.binus.nvjbackend.model.dto.ErrorFieldAndMessageDto;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorController {

  @Autowired
  private ResponseUtil responseUtil;

  @ExceptionHandler(BindException.class)
  public ResponseEntity<RestBaseResponse> handleMethodArgumentNotValidException(BindException e) {
    List<ErrorFieldAndMessageDto> errorFieldAndMessageList = e.getBindingResult()
        .getFieldErrors()
        .stream().map(error -> ErrorFieldAndMessageDto.builder()
              .relatedField(error.getField())
              .errorMessage(error.getDefaultMessage())
              .build())
        .collect(Collectors.toList());
    return responseUtil.buildErrorResponse(null, null, HttpStatus.BAD_REQUEST.value(),
        null, errorFieldAndMessageList);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<RestBaseResponse> handleConstraintViolationException(
      ConstraintViolationException e) {
    List<ErrorFieldAndMessageDto> errorFieldAndMessageList = e.getConstraintViolations()
        .stream().map(this::toErrorFieldAndMessageDto)
        .collect(Collectors.toList());
    return responseUtil.buildErrorResponse(null, null, HttpStatus.BAD_REQUEST.value(),
        null, errorFieldAndMessageList);
  }

  private ErrorFieldAndMessageDto toErrorFieldAndMessageDto(ConstraintViolation violation) {
    List<String> nodes = new ArrayList<>();
    violation.getPropertyPath().iterator().forEachRemaining(node -> nodes.add(node.getName()));
    String fieldName = String.join(".", nodes);
    String errorMessage = violation.getMessage();
    return ErrorFieldAndMessageDto.builder()
        .relatedField(fieldName)
        .errorMessage(errorMessage)
        .build();
  }

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<RestBaseResponse> handleGeneralException(BaseException e) {
    return responseUtil.buildErrorResponse(e);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestBaseResponse> handleException(Exception ex) {
    ErrorCode errorCode = ErrorCode.UNSPECIFIED_ERROR;
    return responseUtil.buildErrorResponse(errorCode.getErrorCode(), errorCode.getDescription(),
        errorCode.getHttpStatus(), null, null);
  }
}
