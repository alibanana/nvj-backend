package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.config.properties.SysparamProperties;
import com.binus.nvjbackend.model.entity.Image;
import com.binus.nvjbackend.model.entity.User;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.RoleRepository;
import com.binus.nvjbackend.repository.UserRepository;
import com.binus.nvjbackend.rest.web.model.request.authentication.LoginRequest;
import com.binus.nvjbackend.rest.web.model.request.authentication.RegisterRequest;
import com.binus.nvjbackend.rest.web.util.EmailTemplateUtil;
import com.binus.nvjbackend.rest.web.util.OtherUtil;
import com.binus.nvjbackend.rest.web.util.PasswordUtil;
import com.binus.nvjbackend.rest.web.util.RoleUtil;
import com.google.zxing.WriterException;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private RoleUtil roleUtil;
  
  @Autowired
  private OtherUtil otherUtil;

  @Autowired
  private PasswordUtil passwordUtil;

  @Autowired
  private EmailTemplateUtil emailTemplateUtil;

  @Autowired
  private SysparamProperties sysparamProperties;

  @Autowired
  private QRCodeService qrCodeService;

  @Autowired
  private FileStorageService fileStorageService;

  @Autowired
  private ImageService imageService;

  @Autowired
  private EmailTemplateService emailTemplateService;

  @Override
  public UserDetails login(LoginRequest loginRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
              loginRequest.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      return (UserDetailsImpl) authentication.getPrincipal();
    } catch (InternalAuthenticationServiceException | BadCredentialsException e) {
      throw new BaseException(ErrorCode.USER_CREDENTIALS_INVALID);
    }
  }

  @Override
  public void register(RegisterRequest registerRequest) throws IOException, WriterException {
    if (userRepository.existsByFirstnameAndLastname(registerRequest.getFirstname(),
        registerRequest.getLastname())) {
      throw new BaseException(ErrorCode.NAME_ALREADY_EXISTS);
    } else if (userRepository.existsByUsername(registerRequest.getUsername())) {
      throw new BaseException(ErrorCode.USERNAME_ALREADY_EXISTS);
    } else if (userRepository.existsByEmail(registerRequest.getEmail())) {
      throw new BaseException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    passwordUtil.validatePasswordValid(registerRequest.getPassword());
    otherUtil.validatePhoneNumber(registerRequest.getPhoneNumber());
    roleUtil.validateRoleType(registerRequest.getRoleType());
    saveNewUser(registerRequest);
  }

  @Override
  public void passwordRecovery(String username) throws TemplateException, MessagingException,
      IOException {
    User user = userRepository.findByUsername(username);
    if (Objects.isNull(user)) {
      throw new BaseException(ErrorCode.USER_NOT_FOUND);
    }
    String newPassword = RandomStringUtils.random(10, true, true);
    user.setPassword(encoder.encode(newPassword));
    userRepository.save(user);
    emailTemplateService.sendEmailTemplate(
        emailTemplateUtil.buildForgetPasswordEmail(user.getEmail(), newPassword));
  }

  private void saveNewUser(RegisterRequest request) throws IOException, WriterException {
    String filename = String.format("qr-%s.png", request.getUsername());
    qrCodeService.generateQRCodeImage(request.getEmail(), filename);
    fileStorageService.validateFileExistsByFilename(filename);
    Image image = imageService.validateAndStoreImageToMongo(
        Paths.get(sysparamProperties.getFileStorageLocation() + filename));
    userRepository.save(buildUser(request, image));
  }

  private User buildUser(RegisterRequest request, Image image) {
    return User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .username(request.getUsername())
        .email(request.getEmail())
        .password(encoder.encode(request.getPassword()))
        .phoneNumber(request.getPhoneNumber())
        .placeOfBirth(request.getPlaceOfBirth())
        .dateOfBirth(request.getDateOfBirth())
        .role(roleRepository.findByRoleType(request.getRoleType()))
        .qrCodeImage(image)
        .build();
  }
}
