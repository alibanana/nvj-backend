package com.binus.nvjbackend.model.enums;

public enum ErrorCode {

  SYSPARAM_KEY_NOT_FOUND("ERR-PA40001", 400,
      "System parameter with the requested key doesn't exists"),

  USER_CREDENTIALS_INVALID("ERR-PA40101", 401,
      "The requested username and password is invalid"),

  SYSPARAM_TYPE_INVALID("ERR-PA40401", 404,
      "The only allowed system parameter type are [STRING, INTEGER, ARRAY_STRING]"),

  SYSPARAM_KEY_ALREADY_EXISTS("ERR-PA42201", 422,
      "System parameter with the requested key already exists"),
  USERNAME_ALREADY_EXISTS("ERR-PA42202", 422,
      "The requested username already exists"),
  EMAIL_ALREADY_EXISTS("ERR-PA42202", 422,
      "The requested email already exists"),

  UNSPECIFIED_ERROR("ERR-PA50001", 500,
      "Unspecified error that is not handled by generid handler");

  private final String errorCode;
  private final int httpStatus;
  private final String description;

  ErrorCode(String errorCode, int httpStatus, String description) {
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public int getHttpStatus() {
    return httpStatus;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
