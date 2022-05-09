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
import com.binus.nvjbackend.rest.web.util.RoleUtil;
import com.google.zxing.WriterException;
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

import java.io.IOException;
import java.nio.file.Paths;

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
  private SysparamProperties sysparamProperties;

  @Autowired
  private QRCodeService qrCodeService;

  @Autowired
  private FileStorageService fileStorageService;

  @Autowired
  private ImageService imageService;

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
    if (userRepository.existsByUsername(registerRequest.getUsername())) {
      throw new BaseException(ErrorCode.USERNAME_ALREADY_EXISTS);
    } else if (userRepository.existsByEmail(registerRequest.getEmail())) {
      throw new BaseException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    roleUtil.validateRoleType(registerRequest.getRoleType());
    saveNewUser(registerRequest);
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
        .username(request.getUsername())
        .email(request.getEmail())
        .password(encoder.encode(request.getPassword()))
        .role(roleRepository.findByRoleType(request.getRoleType()))
        .qrCodeImage(image)
        .build();
  }
}
