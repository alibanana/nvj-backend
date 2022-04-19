package com.binus.nvjbackend.model.dto;

import com.binus.nvjbackend.model.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {

  private String errorCode;
  private String errorMessage;
  private ErrorCode translatedErrorCode;
  private boolean isUseTranslatedError;
}
