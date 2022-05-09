package com.binus.nvjbackend.model.enums;

public enum ErrorCode {

  ROLE_NOT_FOUND("ERR-PA40001", 400,
      "The requested role doesn't exists"),
  SYSPARAM_KEY_NOT_FOUND("ERR-PA40002", 400,
      "System parameter with the requested key doesn't exists"),
  INVALID_REQUEST_PAYLOAD("ERR-PA40003", 400,
      "Requested payload is invalid"),
  ROLE_TYPE_INVALID("ERR-PA40004", 400,
      "The only allowed role type are [ADMIN, MANAGER, EMPLOYEE]"),
  SYSPARAM_TYPE_INVALID("ERR-PA40005", 400,
      "The only allowed system parameter type are [STRING, INTEGER, ARRAY_STRING]"),
  FILE_IS_EMPTY("ERR-PA40006", 400,
      "Failed to store empty file, file must not be empty"),
  FILENAME_INVALID("ERR-PA40007", 400,
      "Filename invalid; cannot store file with relative path outside current directory"),
  FILETYPE_MUST_BE_IMAGE("ERR-PA40008", 400,
      "Only images (png, jpeg/jpg) are allowed, please double check the filetype"),

  USER_CREDENTIALS_INVALID("ERR-PA40101", 401,
      "The requested username and password is invalid"),

  FILE_NOT_FOUND_OR_UNREADABLE("ERR-PA40401", 404,
      "File not found or is un-readable"),
  QR_CODE_NOT_FOUND_OR_UNREADABLE("ERR-PA40402", 404,
      "QRCode not found or is un-readable"),

  FILE_DELETION_FAILED("ERR-PA40901", 409,
      "The requested file cannot be deleted"),

  ROLENAME_ALREADY_EXISTS("ERR-PA42201", 422,
      "Role with the requested name and roleType already exists"),
  SYSPARAM_KEY_ALREADY_EXISTS("ERR-PA42202", 422,
      "System parameter with the requested key already exists"),
  FULLNAME_ALREADY_EXISTS("ERR-PA42203", 422,
      "The requested user's fullname already exists"),
  USERNAME_ALREADY_EXISTS("ERR-PA42204", 422,
      "The requested username already exists"),
  EMAIL_ALREADY_EXISTS("ERR-PA42205", 422,
      "The requested email already exists"),
  FILE_ALREADY_EXISTS("ERR-PA42206", 422,
      "The requested file already exists"),
  TICKET_ALREADY_EXISTS("ERR-PA42207", 422,
      "The requested ticket already exists"),

  UNSPECIFIED_ERROR("ERR-PA50001", 500,
      "Unspecified error that is not handled by generid handler"),
  STORAGE_INITIALIZATION_ERROR("ERR-PA50002", 500,
      "Could not initialize storage directory"),
  FAILED_STORING_FILE("ERR-PA50003", 500,
      "Failed to store file"),
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
