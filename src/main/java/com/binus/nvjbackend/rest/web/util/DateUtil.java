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

  private static final SimpleDateFormat DATE_ONLY_FORMATTER =
      new SimpleDateFormat("yyyy-MM-dd");

  private static final SimpleDateFormat DATE_ONLY_FORMATTER_REVERSE =
      new SimpleDateFormat("dd-MM-yyyy");

  public Date toDateFromMidtrans(String dateInput) throws ParseException {
    return Objects.nonNull(dateInput) ? MIDTRANS_DATETIME_FORMATTER.parse(dateInput) : null;
  }

  public String toDateOnlyFormat(Date date) {
    return DATE_ONLY_FORMATTER.format(date);
  }

  public String toReversedDateOnlyFormat(Date date) {
    return DATE_ONLY_FORMATTER_REVERSE.format(date);
  }

  public Boolean isDateBeforeToday(Date date) {
    date.setHours(0);
    date.setMinutes(0);
    date.setSeconds(1);
    Date today = getDateOnlyForToday();
    return date.before(today);
  }

  private Date getDateOnlyForToday() {
    Date date = new Date();
    date.setHours(0);
    date.setMinutes(0);
    date.setSeconds(0);
    return date;
  }
}
