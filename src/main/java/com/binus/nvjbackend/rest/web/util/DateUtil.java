package com.binus.nvjbackend.rest.web.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Component
public class DateUtil {

  private static final SimpleDateFormat MIDTRANS_DATETIME_FORMATTER =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public Date toDateFromMidtrans(String dateInput) throws ParseException {
    return Objects.nonNull(dateInput) ? MIDTRANS_DATETIME_FORMATTER.parse(dateInput) : null;
  }
}
