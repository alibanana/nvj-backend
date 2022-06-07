package com.binus.nvjbackend.model.enums;

public interface MongoFieldNames {

  // User
  String USER_ID = "id";
  String USER_FIRSTNAME = "firstname";
  String USER_LASTNAME = "lastname";
  String USER_USERNAME = "username";
  String USER_EMAIL = "email";
  String USER_PHONE_NUMBER = "phoneNumber";
  String USER_PLACE_OF_BIRTH = "placeOfBirth";
  String USER_ROLE_TYPE = "role";

  // Ticket
  String TICKET_ID = "id";
  String TICKET_TITLE = "title";
  String TICKET_PRICE = "price";
  String TICKET_PURCHASABLE = "purchasable";
  String TICKET_MARK_FOR_DELETE = "markForDelete";

  // On-Site Experience
  String EXPERIENCE_ID = "id";
  String EXPERIENCE_TITLE = "title";
}
