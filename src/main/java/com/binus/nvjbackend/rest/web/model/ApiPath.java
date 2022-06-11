package com.binus.nvjbackend.rest.web.model;

public class ApiPath {

  public static final String BASE_PATH_API = "/api";

  public static final String BASE_PATH_ROLE = BASE_PATH_API + "/roles";

  public static final String ROLE_INSERT = "/insert";

  public static final String ROLE_FIND_BY_NAME = "/findByName";

  public static final String ROLE_UPDATE_BY_ROLETYPE = "/updateByRoleType";

  public static final String ROLE_DELETE_BY_NAME = "/deleteByName";

  public static final String BASE_PATH_AUTHENTICATION = BASE_PATH_API + "/auth";

  public static final String LOGIN = "/login";

  public static final String REGISTER = "/register";

  public static final String LOGOUT = "/logout";

  public static final String PASSWORD_RECOVERY = "/passwordRecovery";

  public static final String BASE_PATH_USER = BASE_PATH_API + "/users";

  public static final String USER_FIND_BY_FILTER = "/findByFilter";

  public static final String USER_FIND_ALL = "/findAll";

  public static final String USER_FIND_BY_ID = "/findById";

  public static final String USER_UPDATE_BY_ID = "/updateById";

  public static final String USER_DELETE_BY_ID = "/deleteById";

  public static final String CURRENT_USER_DETAILS = "/getCurrentUserDetails";

  public static final String USER_CHANGE_PASSWORD = "/changePassword";

  public static final String BASE_PATH_SYSTEM_PARAMETER = BASE_PATH_API + "/systemparameters";

  public static final String SYSTEM_PARAMETER_INSERT = "/insert";

  public static final String SYSTEM_PARAMETER_FIND_VALUE_BY_KEY = "/findValueByKey";

  public static final String SYSTEM_PARAMETER_UPDATE_BY_KEY = "/updateByKey";

  public static final String SYSTEM_PARAMETER_DELETE_BY_KEY = "/deleteByKey";

  public static final String BASE_PATH_IMAGE = BASE_PATH_API + "/images";

  public static final String UPLOAD_IMAGE = "/upload";

  public static final String GET_IMAGE_BY_FILENAME = "/{filename}";

  public static final String DELETE_IMAGE_BY_FILENAME = "/{filename}";

  public static final String BASE_PATH_EMAIL_TEMPLATE = BASE_PATH_API + "/email-templates";

  public static final String EMAIL_TEMPLATE_CREATE = "/create";

  public static final String EMAIL_TEMPLATE_FIND_BY_TEMPLATE_NAME = "/{templateName}";

  public static final String EMAIL_TEMPLATE_CHECK_BY_TEMPLATE_NAME = "/{templateName}/check";

  public static final String EMAIL_TEMPLATE_UPDATE_BY_TEMPLATE_NAME = "/updateByTemplateName";

  public static final String EMAIL_TEMPLATE_SEND = "/send";

  public static final String EMAIL_TEMPLATE_DELETE_BY_TEMPLATE_NAME = "/{templateName}";

  public static final String BASE_PATH_TICKET = BASE_PATH_API + "/tickets";

  public static final String TICKET_CREATE = "/create";

  public static final String TICKET_FIND_ALL = "/findAll";

  public static final String TICKET_FIND_BY_FILTER = "/findByFilter";

  public static final String TICKET_UPDATE_BY_ID = "/updateById";

  public static final String TICKET_DELETE_BY_ID = "/deleteById";

  public static final String BASE_PATH_EXPERIENCE = BASE_PATH_API + "/experiences";

  public static final String EXPERIENCE_CREATE = "/create";

  public static final String EXPERIENCE_FIND_ALL = "/findAll";

  public static final String EXPERIENCE_FIND_BY_FILTER = "/findByFilter";

  public static final String BASE_PATH_ORDER = BASE_PATH_API + "/orders";

  public static final String ORDER_CREATE = "/create";

  public static final String ORDER_FIND_BY_FILTER = "/findByFilter";

  public static final String ORDER_FIND_ALL = "/findAll";

  public static final String ORDER_UPDATE_BY_ID = "/updateById";
}
