package com.binus.nvjbackend.model.exception;

import com.binus.nvjbackend.model.dto.ErrorDto;
import com.binus.nvjbackend.model.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseException extends RuntimeException {

  private static final long serialVersionUID = -7308323645471082816L;

  private String requestId;
  private String exceptionType;
  private String code;
  private String message;
  private int httpStatus;
  private List<ErrorDto> errorList;

  public BaseException(ErrorCode errorCode, Throwable e) {
    super(e);
    this.code = errorCode.getErrorCode();
    this.message = errorCode.getDescription();
    this.httpStatus = errorCode.getHttpStatus();
    this.exceptionType = e.getClass().getSimpleName();
  }

  public BaseException(ErrorCode errorCode) {
    this.code = errorCode.getErrorCode();
    this.message = errorCode.getDescription();
    this.httpStatus = errorCode.getHttpStatus();
    this.exceptionType = getClass().getSimpleName();
  }

  public BaseException(ErrorCode errorCode, String overriddenMsg) {
    this.code = errorCode.getErrorCode();
    this.message = overriddenMsg;
    this.httpStatus = errorCode.getHttpStatus();
    this.exceptionType = getClass().getSimpleName();
  }

  public BaseException(String code, String message, int httpStatus) {
    this(code, message, httpStatus, null);
  }

  public BaseException(String code, String message, int httpStatus, List<ErrorDto> errorList) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
    this.exceptionType = getClass().getSimpleName();
    this.errorList = errorList;
  }
}
