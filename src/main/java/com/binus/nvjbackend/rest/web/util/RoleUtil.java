package com.binus.nvjbackend.rest.web.util;

import com.binus.nvjbackend.model.entity.Role;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.enums.RoleType;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.RoleRepository;
import com.binus.nvjbackend.rest.web.model.request.role.RoleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RoleUtil {

  @Autowired
  private RoleRepository roleRepository;

  public void validateRoleType(String roleType) {
    if (!RoleType.getAllNames().contains(roleType)) {
      throw new BaseException(ErrorCode.ROLE_TYPE_INVALID);
    }
  }

  public void validateRoleDoesNotExistsByNameAndRoleType(RoleRequest request) {
    if (roleRepository.existsByNameAndRoleType(request.getName(), request.getRoleType())) {
      throw new BaseException(ErrorCode.ROLENAME_ALREADY_EXISTS);
    }
  }

  public void validateRoleNotNull(Role role) {
    if (Objects.isNull(role)) {
      throw new BaseException(ErrorCode.ROLE_NOT_FOUND);
    }
  }

  public void updateRoleWithRequestValue(Role role, RoleRequest request) {
    role.setName(request.getName());
    role.setDescription(request.getDescription());
  }

  public void validateRoleExistsByName(String name) {
    if (!roleRepository.existsByName(name)) {
      throw new BaseException(ErrorCode.ROLE_NOT_FOUND);
    }
  }
}
