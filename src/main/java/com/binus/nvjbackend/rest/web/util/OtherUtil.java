package com.binus.nvjbackend.rest.web.util;

import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Pattern;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Component
public class OtherUtil {

  public void validatePhoneNumber(String phoneNumber) {
    Pattern pattern =
        Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4,9}$");
    if (!pattern.matcher(phoneNumber).matches()) {
      throw new BaseException(ErrorCode.USER_PHONE_NUMBER_INVALID);
    }
  }

  public PageRequest validateAndGetPageRequest(Integer page, Integer size, String orderBy,
      String sortBy) {
    page = validateAndInitializePageNumber(page);
    size = validateAndInitializePageSize(size);
    validateSortByAndOrderBy(sortBy, orderBy);
    return getPageRequest(page, size, orderBy, sortBy);
  }

  private int validateAndInitializePageNumber(Integer page) {
    if (Objects.nonNull(page) && page < 0) {
      throw new BaseException(ErrorCode.PAGE_NUMBER_LESS_THAN_ZERO);
    }
    return Objects.isNull(page) ? 0 : page;
  }

  private int validateAndInitializePageSize(Integer size) {
    if (Objects.nonNull(size) && size <= 0) {
      throw new BaseException(ErrorCode.PAGE_SIZE_LESS_THAN_OR_EQUAL_TO_ZERO);
    }
    return Objects.isNull(size) ? 10 : size;
  }

  private void validateSortByAndOrderBy(String sortBy, String orderBy) {
    if (Objects.nonNull(sortBy) && !sortBy.equals(ASC.name()) && !sortBy.equals(DESC.name())) {
      throw new BaseException(ErrorCode.SORT_BY_VALUES_INVALID);
    }

    if ((Objects.nonNull(sortBy) && Objects.isNull(orderBy)) || (Objects.isNull(sortBy) &&
        Objects.nonNull(orderBy))) {
      throw new BaseException(ErrorCode.SORT_BY_AND_ORDER_BY_MUST_BOTH_EXISTS);
    }
  }

  private PageRequest getPageRequest(Integer page, Integer size, String orderBy, String sortBy) {
    if (Objects.isNull(orderBy) && Objects.isNull(sortBy)) {
      return PageRequest.of(page, size);
    } else {
      return PageRequest.of(page, size, Sort.Direction.fromString(sortBy), orderBy);
    }
  }
}
