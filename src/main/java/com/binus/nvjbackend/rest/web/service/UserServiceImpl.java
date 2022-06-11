package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.config.properties.SysparamProperties;
import com.binus.nvjbackend.model.entity.Image;
import com.binus.nvjbackend.model.entity.User;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.UserRepository;
import com.binus.nvjbackend.rest.web.model.request.user.UserChangePasswordRequest;
import com.binus.nvjbackend.rest.web.model.request.user.UserFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.user.UserRequest;
import com.binus.nvjbackend.rest.web.util.JwtUtil;
import com.binus.nvjbackend.rest.web.util.OtherUtil;
import com.binus.nvjbackend.rest.web.util.PasswordUtil;
import com.binus.nvjbackend.rest.web.util.RoleUtil;
import com.google.zxing.WriterException;
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

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private SysparamProperties sysparamProperties;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private OtherUtil otherUtil;

  @Autowired
  private RoleUtil roleUtil;

  @Autowired
  private PasswordUtil passwordUtil;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleService roleService;

  @Autowired
  private ImageService imageService;

  @Autowired
  private QRCodeService qrCodeService;

  @Autowired
  private FileStorageService fileStorageService;

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
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public User findById(String id) {
    User user = Optional.of(userRepository.findById(id)).get()
        .orElse(null);
    if (Objects.isNull(user)) {
      throw new BaseException(ErrorCode.USER_NOT_FOUND);
    }
    return user;
  }

  @Override
  public User updateById(String id, UserRequest request) throws IOException, WriterException {
    User user = findById(id);
    validateNewFirstnameUnique(user.getFirstname(), request.getFirstname());
    validateNewLastnameUnique(user.getLastname(), request.getLastname());
    validateNewUsernameUnique(user.getUsername(), request.getUsername());
    validateNewEmailUnique(user.getEmail(), request.getEmail());
    otherUtil.validatePhoneNumber(request.getPhoneNumber());
    roleUtil.validateRoleType(request.getRoleType());
    return updateUser(user, request);
  }

  @Override
  public void deleteById(String id) {
    User user = findById(id);
    imageService.deleteById(user.getQrCodeImage().getId());
    userRepository.deleteById(id);
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
    passwordUtil.validatePasswordValid(request.getNewPassword());
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

  private void validateNewFirstnameUnique(String currentFirstname, String newFirstname) {
    if (!currentFirstname.equals(newFirstname) && userRepository.existsByFirstname(newFirstname)) {
      throw new BaseException(ErrorCode.NEW_USER_FIRSTNAME_EXISTS);
    }
  }

  private void validateNewLastnameUnique(String currentLastname, String newLastname) {
    if (!currentLastname.equals(newLastname) && userRepository.existsByLastname(newLastname)) {
      throw new BaseException(ErrorCode.NEW_USER_LASTNAME_EXISTS);
    }
  }

  private void validateNewUsernameUnique(String currentUsername, String newUsername) {
    if (!currentUsername.equals(newUsername) && userRepository.existsByUsername(newUsername)) {
      throw new BaseException(ErrorCode.NEW_USER_USERNAME_EXISTS);
    }
  }

  private void validateNewEmailUnique(String currentEmail, String newEmail) {
    if (!currentEmail.equals(newEmail) && userRepository.existsByEmail(newEmail)) {
      throw new BaseException(ErrorCode.NEW_USER_EMAIL_EXISTS);
    }
  }

  private User updateUser(User user, UserRequest request) throws IOException, WriterException {
    Image image = updateUserQrCodeImage(user, request);
    user.setFirstname(request.getFirstname());
    user.setLastname(request.getLastname());
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPhoneNumber(request.getPhoneNumber());
    user.setPlaceOfBirth(request.getPlaceOfBirth());
    user.setDateOfBirth(request.getDateOfBirth());
    user.setRole(roleService.findByRoleType(request.getRoleType()));
    if (Objects.nonNull(image)) {
      user.setQrCodeImage(image);
    }
    return userRepository.save(user);
  }

  private Image updateUserQrCodeImage(User user, UserRequest request) throws IOException,
      WriterException {
    // If either username or email has been changed
    if (!user.getUsername().equals(request.getUsername()) ||
        !user.getEmail().equals(request.getEmail())) {
      imageService.deleteById(user.getQrCodeImage().getId());
      String filename = String.format("qr-%s.png", request.getUsername());
      qrCodeService.generateQRCodeImage(request.getEmail(), filename);
      fileStorageService.validateFileExistsByFilename(filename);
      return imageService.validateAndStoreImageToMongo(
          Paths.get(sysparamProperties.getFileStorageLocation() + filename));
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
