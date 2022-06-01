package com.binus.nvjbackend.rest.web.controller.server;

import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.request.authentication.LoginRequest;
import com.binus.nvjbackend.rest.web.model.request.authentication.RegisterRequest;
import com.binus.nvjbackend.rest.web.model.response.LoginResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.AuthenticationService;
import com.binus.nvjbackend.rest.web.service.UserDetailsImpl;
import com.binus.nvjbackend.rest.web.util.JwtUtil;
import com.google.zxing.WriterException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@Api(value = "Authentication", description = "Authentication Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_AUTHENTICATION)
public class AuthenticationController extends BaseController {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping(value = ApiPath.LOGIN)
  public ResponseEntity<RestSingleResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
    UserDetailsImpl userDetails = (UserDetailsImpl) authenticationService.login(request);
    ResponseCookie jwtCookie = jwtUtil.generateJwtCookie(userDetails);
    LoginResponse response = toLoginResponse(userDetails, jwtCookie);
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(toSingleResponse(response));
  }

  private LoginResponse toLoginResponse(UserDetailsImpl userDetails, ResponseCookie jwtCookie) {
    return LoginResponse.builder()
        .id(userDetails.getId())
        .username(userDetails.getUsername())
        .email(userDetails.getEmail())
        .token(jwtCookie.getValue())
        .phoneNumber(userDetails.getPhoneNumber())
        .placeOfBirth(userDetails.getPlaceOfBirth())
        .dateOfBirth(userDetails.getDateOfBirth())
        .build();
  }

  @PostMapping(value = ApiPath.REGISTER)
  public RestBaseResponse register(@Valid @RequestBody RegisterRequest request)
      throws IOException, WriterException {
    authenticationService.register(request);
    return toBaseResponse();
  }

  @PostMapping(value = ApiPath.LOGOUT)
  public ResponseEntity<RestBaseResponse> logout() {
    ResponseCookie cookie = jwtUtil.getCleanJwtCookie();
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(toBaseResponse());
  }
}
