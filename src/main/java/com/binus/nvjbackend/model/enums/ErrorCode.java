package com.binus.nvjbackend.model.enums;

public enum ErrorCode {

  SYSPARAM_KEY_NOT_FOUND("ERR-PA40001", 400,
      "System parameter with the requested key doesn't exists"),

  USER_CREDENTIALS_INVALID("ERR-PA40101", 401,
      "The requested username and password is invalid"),

  INVALID_REQUEST_PAYLOAD("ERR-PA40401", 404,
      "Requested payload is invalid"),
  SYSPARAM_TYPE_INVALID("ERR-PA40402", 404,
      "The only allowed system parameter type are [STRING, INTEGER, ARRAY_STRING]"),
  FILE_IS_EMPTY("ERR-PA40403", 404,
      "Failed to store empty file"),
  FILENAME_INVALID("ERR-PA40404", 404,
      "Cannot store file with relative path outside current directory"),
  FILETYPE_MUST_BE_IMAGE("ERR-PA40405", 404,
      "Only images (png, jpeg/jpg) are allowed, please double check the filetype"),

  SYSPARAM_KEY_ALREADY_EXISTS("ERR-PA42201", 422,
      "System parameter with the requested key already exists"),
  USERNAME_ALREADY_EXISTS("ERR-PA42202", 422,
      "The requested username already exists"),
  EMAIL_ALREADY_EXISTS("ERR-PA42202", 422,
      "The requested email already exists"),

  UNSPECIFIED_ERROR("ERR-PA50001", 500,
      "Unspecified error that is not handled by generid handler"),
  STORAGE_INITIALIZATION_ERROR("ERR-PA50002", 500,
      "Could not initialize storage directory"),
  FAILED_STORING_FILE("ERR-PA50003", 500,
      "Failed to store file"),
  FILE_NOT_FOUND_OR_UNREADABLE("ERR-PA50004", 500,
      "File not found or is un-readable"),
  FAILED_STORING_IMAGE("ERR-PA50005", 500,
      "Failed to store image to database");

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
