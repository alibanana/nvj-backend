package com.binus.nvjbackend.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum RoleType {

  ADMIN,
  MANAGER,
  EMPLOYEE;

  public static List<String> getAllNames() {
    return Arrays.stream(RoleType.values())
        .map(RoleType::name)
        .collect(Collectors.toList());
  }
}
