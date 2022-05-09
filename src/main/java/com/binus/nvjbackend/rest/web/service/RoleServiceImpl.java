package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Role;
import com.binus.nvjbackend.model.enums.RoleType;
import com.binus.nvjbackend.repository.RoleRepository;
import com.binus.nvjbackend.rest.web.model.request.role.RoleRequest;
import com.binus.nvjbackend.rest.web.util.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private RoleUtil roleUtil;

  @Override
  public void insert(RoleRequest request) {
    roleUtil.validateRoleType(request.getRoleType());
    roleUtil.validateRoleDoesNotExistsByNameAndRoleType(request);
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
    roleUtil.validateRoleNotNull(role);
    return role;
  }

  @Override
  public void updateByRoleType(RoleRequest request) {
    roleUtil.validateRoleType(request.getRoleType());
    Role role = roleRepository.findByRoleType(request.getRoleType());
    roleUtil.validateRoleNotNull(role);
    roleUtil.updateRoleWithRequestValue(role, request);
    roleRepository.save(role);
  }

  @Override
  public void deleteByName(String name) {
    roleUtil.validateRoleExistsByName(name);
    roleRepository.deleteByName(name);
  }
}
