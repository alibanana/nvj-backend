package com.binus.nvjbackend.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SystemParameterTypes {
  STRING,
  INTEGER,
  ARRAY_STRING;

  public static List<String> getAllNames() {
    return Arrays.stream(SystemParameterTypes.values())
        .map(SystemParameterTypes::name)
        .collect(Collectors.toList());
  }
}
