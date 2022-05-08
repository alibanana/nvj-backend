package com.binus.nvjbackend.rest.web.controller;

import com.binus.nvjbackend.model.entity.Role;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.request.role.RoleNameRequest;
import com.binus.nvjbackend.rest.web.model.request.role.RoleRequest;
import com.binus.nvjbackend.rest.web.model.response.RoleResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestListResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.RoleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Roles", description = "Roles Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_ROLE)
public class RoleController extends BaseController {

  @Autowired
  private RoleService roleService;

  @PostMapping(value = ApiPath.ROLE_INSERT)
  public RestBaseResponse insert(@Valid @RequestBody RoleRequest request) {
    roleService.insert(request);
    return toBaseResponse();
  }

  @GetMapping
  public RestListResponse<RoleResponse> findAll() {
    List<Role> roles = roleService.findAll();
    return toListResponse(toRoleListResponse(roles));
  }

  @PostMapping(value = ApiPath.ROLE_FIND_BY_NAME)
  public RestSingleResponse<RoleResponse> findByName(@Valid @RequestBody RoleNameRequest request) {
    Role role = roleService.findByName(request.getName());
    return toSingleResponse(toRoleSingleResponse(role));
  }

  @PutMapping(value = ApiPath.ROLE_UPDATE_BY_ROLETYPE)
  public RestBaseResponse updateByRoleType(@Valid @RequestBody RoleRequest request) {
    roleService.updateByRoleType(request);
    return toBaseResponse();
  }

  @DeleteMapping(value = ApiPath.ROLE_DELETE_BY_NAME)
  public RestBaseResponse deleteByName(@Valid @RequestBody RoleNameRequest request) {
    roleService.deleteByName(request.getName());
    return toBaseResponse();
  }

  private List<RoleResponse> toRoleListResponse(List<Role> roles) {
    return roles.stream()
        .map(role -> RoleResponse.builder()
            .name(role.getName())
            .roleType(role.getRoleType().name())
            .description(role.getDescription())
            .build())
        .collect(Collectors.toList());
  }

  private RoleResponse toRoleSingleResponse(Role role) {
    return RoleResponse.builder()
        .name(role.getName())
        .roleType(role.getRoleType().name())
        .description(role.getDescription())
        .build();
  }
}
