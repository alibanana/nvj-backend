package com.binus.nvjbackend.model.exception;

import com.binus.nvjbackend.model.enums.ErrorCode;

public class DataNotFoundException extends BaseException {

    public DataNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DataNotFoundException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
