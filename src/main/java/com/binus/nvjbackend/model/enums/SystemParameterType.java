package com.binus.nvjbackend.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SystemParameterType {
  STRING,
  INTEGER,
  ARRAY_STRING;

  public static List<String> getAllNames() {
    return Arrays.stream(SystemParameterType.values())
        .map(SystemParameterType::name)
        .collect(Collectors.toList());
  }
}
