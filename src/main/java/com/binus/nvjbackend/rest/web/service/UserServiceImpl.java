package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.User;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.UserRepository;
import com.binus.nvjbackend.rest.web.model.request.user.UserChangePasswordRequest;
import com.binus.nvjbackend.rest.web.model.request.user.UserFilterRequest;
import com.binus.nvjbackend.rest.web.util.JwtUtil;
import com.binus.nvjbackend.rest.web.util.OtherUtil;
import com.binus.nvjbackend.rest.web.util.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private PasswordEncoder encoder;

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

  @Override
  public User changePassword(String token, UserChangePasswordRequest request) {
    String username = jwtUtil.getUsernameFromJwtToken(token);
    User user = userRepository.findByUsername(username);
    if (Objects.isNull(user)) {
      throw new BaseException(ErrorCode.USER_NOT_FOUND);
    }
    validateUsernameAndPassword(username, request.getPassword());
    validatePasswordRequest(username, request);
    user.setPassword(encoder.encode(request.getNewPassword()));
    return userRepository.save(user);
  }

  private String validateRoleTypeAndReturnRoleId(String roleType) {
    if (Objects.nonNull(roleType) && StringUtils.hasText(roleType)) {
      roleUtil.validateRoleType(roleType);
      return roleService.findByRoleType(roleType).getId();
    }
    return null;
  }

  private void validateUsernameAndPassword(String username, String password) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
    } catch (InternalAuthenticationServiceException | BadCredentialsException e) {
      throw new BaseException(ErrorCode.USER_PASSWORD_INVALID);
    }
  }

  private void validatePasswordRequest(String username, UserChangePasswordRequest request) {
    if (request.getPassword().equals(request.getNewPassword())) {
      throw new BaseException(ErrorCode.PASSWORD_AND_NEW_PASSWORD_SAME);
    } else if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
      throw new BaseException(ErrorCode.NEW_PASSWORD_AND_CONFIRM_PASSWORD_DIFFERENT);
    }
  }
}
