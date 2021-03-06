package com.binus.nvjbackend.model.enums;

public enum ErrorCode {

  INVALID_REQUEST_PAYLOAD("ERR-PA40001", 400,
      "Requested payload is invalid"),
  ROLE_TYPE_INVALID("ERR-PA40002", 400,
      "The only allowed role type are [ADMIN, MANAGER, EMPLOYEE]"),
  SYSPARAM_TYPE_INVALID("ERR-PA40003", 400,
      "The only allowed system parameter type are [STRING, INTEGER, ARRAY_STRING]"),
  FILE_IS_EMPTY("ERR-PA40004", 400,
      "Failed to store empty file, file must not be empty"),
  FILENAME_INVALID("ERR-PA40005", 400,
      "Filename invalid; cannot store file with relative path outside current directory"),
  FILETYPE_MUST_BE_IMAGE("ERR-PA40006", 400,
      "Only images (png, jpeg/jpg) are allowed, please double check the filetype"),
  EMAIL_TEMPLATE_KEY_VALUES_MISSING("ERR-PA40007", 400,
      "Some key & value pairs are missing for the requested email template"),
  PAGE_NUMBER_LESS_THAN_ZERO("ERR-40008", 400,
      "Page number must not be less than zero"),
  PAGE_SIZE_LESS_THAN_OR_EQUAL_TO_ZERO("ERR-40009", 400,
      "Page size must be greater than zero"),
  SORT_BY_VALUES_INVALID("ERR-40010", 400,
      "The only allowed sortBy values are [ASC, DESC]"),
  ORDER_ID_MISSING("ERR-PA40011", 400,
      "Order id is missing from the request body"),
  ORDER_SIGNATURE_KEY_MISSING("ERR-PA40012", 400,
      "Signature key is missing from the request body"),
  USER_PHONE_NUMBER_INVALID("ERR-PA40013", 400,
      "Phone number requested is invalid, please re-check"),
  SORT_BY_AND_ORDER_BY_MUST_BOTH_EXISTS("ERR-PA40014", 400,
      "Both sort by and order by must be filled"),
  TICKETS_ARE_NOT_PURCHASABLE("ERR-PA40015", 400,
      "One or more of the tickets requested are not purchasable"),
  ORDER_VISIT_DATE_BEFORE_TODAY("ERR-PA40016", 400,
      "Visit Date must be greater than or equal to today"),
  PASSWORD_AND_NEW_PASSWORD_SAME("ERR-PA40017", 400,
      "New password must be different than the old password"),
  NEW_PASSWORD_AND_CONFIRM_PASSWORD_DIFFERENT("ERR-PA40018", 400,
      "New password and confirm new password must be the same"),
  PASSWORD_LENGTH_INVALID("ERR-PA40019", 400,
      "Password must be 8 - 16 characters in length"),
  PASSWORD_UPPERCASE_COUNT_INVALID("ERR-PA40020", 400,
      "Password must have one or more uppercase character"),
  PASSWORD_LOWERCASE_COUNT_INVALID("ERR-PA40021", 400,
      "Password must have one or more lowercase character"),
  PASSWORD_DIGIT_COUNT_INVALID("ERR-PA40022", 400,
      "Password must have one or more numerical character"),
  ORDER_MUST_BE_MANUAL_ORDER("ERR-PA40023", 400,
      "The requested order is not a manual order"),
  USER_EMAIL_INVALID("ERR-PA40024", 400,
      "Email requested is invalid, please re-check"),

  USER_CREDENTIALS_INVALID("ERR-PA40101", 401,
      "The requested username and password is invalid"),
  ORDER_SIGNATURE_KEY_INVALID("ERR-PA40102", 401,
      "The requested signature_key is invalid"),
  USER_PASSWORD_INVALID("ERR-PA40103", 401,
      "The requested user password is invalid"),

  ROLE_NOT_FOUND("ERR-PA40401", 404,
      "The requested role doesn't exists"),
  SYSPARAM_KEY_NOT_FOUND("ERR-PA40402", 404,
      "System parameter with the requested key doesn't exists"),
  FILE_NOT_FOUND_OR_UNREADABLE("ERR-PA40403", 404,
      "File not found or is un-readable"),
  TICKET_NOT_FOUND("ERR-PA40404", 404,
      "The requested ticket does not exists"),
  EMAIL_TEMPLATE_NAME_NOT_FOUND("ERR-PA40405", 404,
      "Email template with the requested template name doesn't exists"),
  ON_SITE_EXPERIENCE_NOT_FOUND("ERR-PA40406", 404,
      "The requested on-site experience does not exists"),
  MIDTRANS_ORDER_ID_NOT_FOUND("ERR-PA40407", 404,
      "The requested midtrans-orderId does not exists"),
  USER_NOT_FOUND("ERR-PA40408", 404,
      "The requested user does not exists"),
  IMAGE_NOT_FOUND("ERR-PA40409", 404,
      "The requested image does not exists"),
  ORDER_ID_NOT_FOUND("ERR-PA40410", 404,
      "The requested order id does not exists"),
  ON_SITE_EXPERIENCE_MAX_IMAGES_EXCEEDED("ERR-PA40410", 404,
      "There are already 4 images on the requested on-site experience object"),

  FILE_DELETION_FAILED("ERR-PA40901", 409,
      "The requested file cannot be deleted"),

  ROLENAME_ALREADY_EXISTS("ERR-PA42201", 422,
      "Role with the requested name and roleType already exists"),
  SYSPARAM_KEY_ALREADY_EXISTS("ERR-PA42202", 422,
      "System parameter with the requested key already exists"),
  NAME_ALREADY_EXISTS("ERR-PA42203", 422,
      "The requested user's firstname and lastname has already been taken"),
  USERNAME_ALREADY_EXISTS("ERR-PA42204", 422,
      "The requested username already exists"),
  EMAIL_ALREADY_EXISTS("ERR-PA42205", 422,
      "The requested email already exists"),
  FILE_ALREADY_EXISTS("ERR-PA42206", 422,
      "The requested file already exists"),
  TICKET_ALREADY_EXISTS("ERR-PA42207", 422,
      "The requested ticket already exists"),
  EMAIL_TEMPLATE_EXISTS("ERR-PA42208", 422,
      "The requested template name already exists"),
  ON_SITE_EXPERIENCE_EXISTS("ERR-PA42209", 422,
      "The requested on-site experience title already exists"),
  NEW_TICKET_TITLE_EXISTS("ERR-PA42210", 422,
      "The requested ticket title already exists"),
  NEW_USER_FIRSTNAME_EXISTS("ERR-PA42211", 422,
      "The requested new user's firstname already exists"),
  NEW_USER_LASTNAME_EXISTS("ERR-PA42212", 422,
      "The requested new user's lastname already exists"),
  NEW_USER_USERNAME_EXISTS("ERR-PA42213", 422,
      "The requested new user's username already exists"),
  NEW_USER_EMAIL_EXISTS("ERR-PA42214", 422,
      "The requested new user's email already exists"),

  UNSPECIFIED_ERROR("ERR-PA50001", 500,
      "Unspecified error that is not handled by generid handler"),
  STORAGE_INITIALIZATION_ERROR("ERR-PA50002", 500,
      "Could not initialize storage directory"),
  FAILED_STORING_FILE("ERR-PA50003", 500,
      "Failed to store file"),
  FAILED_STORING_IMAGE("ERR-PA50004", 500,
      "Failed to store image to database"),
  MAX_UPLOAD_SIZE_EXCEEDED("ERR-PA50005", 500,
      "Request failed due to max upload size exceeded");

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
