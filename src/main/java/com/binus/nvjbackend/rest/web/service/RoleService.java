package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Role;
import com.binus.nvjbackend.rest.web.model.request.role.RoleRequest;

import java.util.List;

public interface RoleService {

  void insert(RoleRequest request);

  List<Role> findAll();

  Role findByName(String name);

  Role findByRoleType(String roleType);

  void updateByRoleType(RoleRequest request);

  void deleteByName(String name);
}
