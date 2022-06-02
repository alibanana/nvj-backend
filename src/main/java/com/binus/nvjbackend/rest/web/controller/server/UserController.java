package com.binus.nvjbackend.rest.web.controller.server;

import com.binus.nvjbackend.model.entity.User;
import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.response.RoleResponse;
import com.binus.nvjbackend.rest.web.model.response.UserResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(value = "User", description = "User Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_USER)
public class UserController extends BaseController {

  @Autowired
  private UserService userService;

  @PostMapping(value = ApiPath.CURRENT_USER_DETAILS)
  public RestSingleResponse<UserResponse> getCurrentUserDetails(HttpServletRequest request) {
    String token = request.getHeader("Authorization").substring(7);
    User user = userService.getCurrentUserDetails(token);
    return toSingleResponse(toUserSingleResponse(user, token));
  }

  private UserResponse toUserSingleResponse(User user, String token) {
    return UserResponse.builder()
        .id(user.getId())
        .firstname(user.getFirstname())
        .lastname(user.getLastname())
        .username(user.getUsername())
        .email(user.getEmail())
        .token(token)
        .phoneNumber(user.getPhoneNumber())
        .placeOfBirth(user.getPlaceOfBirth())
        .dateOfBirth(user.getDateOfBirth())
        .role(RoleResponse.builder()
            .name(user.getRole().getName())
            .roleType(user.getRole().getRoleType().name())
            .description(user.getRole().getDescription())
            .build())
        .qrCodeImageUrl(user.getQrCodeImage().getUrl())
        .build();
  }
}
