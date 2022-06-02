package com.binus.nvjbackend.rest.web.util;

import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class OtherUtil {

  public void validatePhoneNumber(String phoneNumber) {
    Pattern pattern =
        Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4,9}$");
    if (!pattern.matcher(phoneNumber).matches()) {
      throw new BaseException(ErrorCode.USER_PHONE_NUMBER_INVALID);
    }
  }
}
