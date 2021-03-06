package com.binus.nvjbackend.rest.web.model;

public class ApiPathClient {

  public static final String BASE_PATH_API = "/api/client";

  public static final String BASE_PATH_TICKET = BASE_PATH_API + "/tickets";

  public static final String TICKET_FIND_BY_FILTER = "/findByFilter";

  public static final String TICKET_FIND_BY_IDS = "/findByIds";

  public static final String BASE_PATH_EXPERIENCE = BASE_PATH_API + "/experiences";

  public static final String EXPERIENCE_FIND_ALL = "/findAll";

  public static final String EXPERIENCE_FIND_BY_ID = "/findById";

  public static final String BASE_PATH_ORDER = BASE_PATH_API + "/orders";

  public static final String ORDER_CREATE = "/create";

  public static final String ORDER_HANDLE_NOTIFICATION = "/notification/handle";

  public static final String ORDER_DETAIL_BY_MIDTRANS_ORDER_ID = "/getDetailsByMidtransOrderId";

  public static final String RESEND_EMAIL_BY_MIDTRANS_ORDER_ID = "/resendEmailByMidtransOrderId";
}
