package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Role;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.enums.RoleType;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.RoleRepository;
import com.binus.nvjbackend.rest.web.model.request.role.RoleNameRequest;
import com.binus.nvjbackend.rest.web.model.request.role.RoleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public void insert(RoleRequest request) {
    validateRoleRequest(request);
    validateRoleDoesNotExistsByNameAndRoleType(request);
    Role role = Role.builder()
        .name(request.getName())
        .roleType(RoleType.valueOf(request.getRoleType()))
        .description(request.getDescription())
        .build();
    roleRepository.save(role);
  }

  @Override
  public List<Role> findAll() {
    return roleRepository.findAll();
  }

  @Override
  public Role findByName(String name) {
    Role role = roleRepository.findByName(name);
    validateRoleNotNull(role);
    return role;
  }

  @Override
  public void updateByRoleType(RoleRequest request) {
    validateRoleRequest(request);
    Role role = roleRepository.findByRoleType(request.getRoleType());
    validateRoleNotNull(role);
    updateRoleWithRequestValue(role, request);
    roleRepository.save(role);
  }

  @Override
  public void deleteByName(String name) {
    validateRoleExistsByName(name);
    roleRepository.deleteByName(name);
  }

  private void validateRoleRequest(RoleRequest request) {
    if (!RoleType.getAllNames().contains(request.getRoleType())) {
      throw new BaseException(ErrorCode.ROLE_TYPE_INVALID);
    }
  }

  private void validateRoleDoesNotExistsByNameAndRoleType(RoleRequest request) {
    if (roleRepository.existsByNameAndRoleType(request.getName(), request.getRoleType())) {
      throw new BaseException(ErrorCode.ROLENAME_ALREADY_EXISTS);
    }
  }

  private void validateRoleNotNull(Role role) {
    if (Objects.isNull(role)) {
      throw new BaseException(ErrorCode.ROLE_NOT_FOUND);
    }
  }

  private void updateRoleWithRequestValue(Role role, RoleRequest request) {
    role.setName(request.getName());
    role.setDescription(request.getDescription());
  }

  private void validateRoleExistsByName(String name) {
    if (!roleRepository.existsByName(name)) {
      throw new BaseException(ErrorCode.ROLE_NOT_FOUND);
    }
  }
}
