package com.binus.nvjbackend.rest.web.model;

public class ApiPath {

  public static final String BASE_PATH_API = "/api";

  public static final String BASE_PATH_AUTHENTICATION = BASE_PATH_API + "/auth";

  public static final String LOGIN = "/login";

  public static final String REGISTER = "/register";

  public static final String LOGOUT = "/logout";

  public static final String BASE_PATH_SYSTEM_PARAMETER = BASE_PATH_API + "/systemparameter";

  public static final String SYSTEM_PARAMETER_INSERT = "/insert";

  public static final String SYSTEM_PARAMETER_FIND_VALUE_BY_KEY = "/findValueByKey";

  public static final String SYSTEM_PARAMETER_UPDATE_BY_KEY = "/updateByKey";

  public static final String SYSTEM_PARAMETER_DELETE_BY_KEY = "/deleteByKey";

  public static final String BASE_PATH_IMAGE = BASE_PATH_API + "/image";

  public static final String UPLOAD_IMAGE = "/upload";
}
