package com.binus.nvjbackend.rest.web.util;

import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

  private final int MIN_LENGTH = 8;
  private final int MAX_LENGTH = 16;

  public void validatePasswordValid(String password) {
    if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
      throw new BaseException(ErrorCode.PASSWORD_LENGTH_INVALID);
    }

    int upCount = 0; int lowCount = 0; int digit = 0;
    for (int i = 0; i < password.length(); i++) {
      char c = password.charAt(i);
      if (Character.isUpperCase(c)) {
        upCount++;
      } else if (Character.isLowerCase(c)) {
        lowCount++;
      } else if (Character.isDigit(c)) {
        digit++;
      }
    }

    if (upCount == 0) {
      throw new BaseException(ErrorCode.PASSWORD_UPPERCASE_COUNT_INVALID);
    } else if (lowCount == 0) {
      throw new BaseException(ErrorCode.PASSWORD_LOWERCASE_COUNT_INVALID);
    } else if (digit == 0) {
      throw new BaseException(ErrorCode.PASSWORD_DIGIT_COUNT_INVALID);
    }
  }
}
