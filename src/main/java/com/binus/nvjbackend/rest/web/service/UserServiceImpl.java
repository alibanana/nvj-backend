package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.User;
import com.binus.nvjbackend.repository.UserRepository;
import com.binus.nvjbackend.rest.web.model.request.user.UserFilterRequest;
import com.binus.nvjbackend.rest.web.util.JwtUtil;
import com.binus.nvjbackend.rest.web.util.OtherUtil;
import com.binus.nvjbackend.rest.web.util.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private OtherUtil otherUtil;

  @Autowired
  private RoleUtil roleUtil;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleService roleService;

  @Override
  public Page<User> findByFilter(Integer page, Integer size, String orderBy, String sortBy,
      UserFilterRequest request) {
    PageRequest pageRequest = otherUtil.validateAndGetPageRequest(page, size, orderBy, sortBy);
    String roleId = validateRoleTypeAndReturnRoleId(request.getRoleType());
    return userRepository.findAllByIdAndFirstnameAndLastnameAndUsernameAndEmailAndPhoneNumberAndPlaceOfBirth(
        request.getId(), request.getFirstname(), request.getLastname(), request.getUsername(),
        request.getEmail(), request.getPhoneNumber(), request.getPlaceOfBirth(), roleId,
        pageRequest);
  }

  @Override
  public User getCurrentUserDetails(String token) {
    String username = jwtUtil.getUsernameFromJwtToken(token);
    return userRepository.findByUsername(username);
  }

  private String validateRoleTypeAndReturnRoleId(String roleType) {
    if (Objects.nonNull(roleType) && StringUtils.hasText(roleType)) {
      roleUtil.validateRoleType(roleType);
      return roleService.findByRoleType(roleType).getId();
    }
    return null;
  }
}
