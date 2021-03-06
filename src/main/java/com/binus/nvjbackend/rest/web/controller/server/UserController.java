package com.binus.nvjbackend.rest.web.controller.server;

import com.binus.nvjbackend.model.entity.User;
import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.request.user.UserChangePasswordRequest;
import com.binus.nvjbackend.rest.web.model.request.user.UserFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.user.UserRequest;
import com.binus.nvjbackend.rest.web.model.response.RoleResponse;
import com.binus.nvjbackend.rest.web.model.response.UserResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestListResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestPageResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.UserService;
import com.binus.nvjbackend.rest.web.util.DateUtil;
import com.google.zxing.WriterException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(value = "User", description = "User Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_USER)
@Validated
public class UserController extends BaseController {

  @Autowired
  private UserService userService;

  @Autowired
  private DateUtil dateUtil;

  @PostMapping(value = ApiPath.USER_FIND_BY_FILTER)
  public RestPageResponse<UserResponse> findByFilter(@RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size, @RequestParam(required = false) String orderBy,
      @RequestParam(required = false) String sortBy,
      @RequestBody UserFilterRequest request) {
    Page<User> users = userService.findByFilter(page, size, orderBy, sortBy, request);
    List<UserResponse> userResponses = users.getContent().stream()
        .map(this::toUserResponse)
        .collect(Collectors.toList());
    return toPageResponse(userResponses, users);
  }

  @PostMapping(value = ApiPath.USER_FIND_ALL)
  public RestListResponse<UserResponse> findAll() {
    List<User> users = userService.findAll();
    return toListResponse(users.stream()
        .map(this::toUserResponse)
        .collect(Collectors.toList()));
  }

  @PostMapping(value = ApiPath.USER_FIND_BY_ID)
  public RestSingleResponse<UserResponse> findById(@NotBlank @RequestParam String id) {
    User user = userService.findById(id);
    return toSingleResponse(toUserResponse(user));
  }

  @PutMapping(value = ApiPath.USER_UPDATE_BY_ID)
  public RestSingleResponse<UserResponse> updateById(@NotBlank @RequestParam String id,
      @Valid @RequestBody UserRequest request) throws IOException, WriterException {
    User user = userService.updateById(id, request);
    return toSingleResponse(toUserResponse(user));
  }

  @DeleteMapping(value = ApiPath.USER_DELETE_BY_ID)
  public RestBaseResponse deleteById(@NotBlank @RequestParam String id) {
    userService.deleteById(id);
    return toBaseResponse();
  }

  @PostMapping(value = ApiPath.CURRENT_USER_DETAILS)
  public RestSingleResponse<UserResponse> getCurrentUserDetails(HttpServletRequest request) {
    String token = request.getHeader("Authorization").substring(7);
    User user = userService.getCurrentUserDetails(token);
    return toSingleResponse(toUserResponse(user, token));
  }

  @PostMapping(value = ApiPath.USER_CHANGE_PASSWORD)
  public RestBaseResponse changePassword(HttpServletRequest httpServletRequest,
      @Valid @RequestBody UserChangePasswordRequest request) {
    String token = httpServletRequest.getHeader("Authorization").substring(7);
    User user = userService.changePassword(token, request);
    return toSingleResponse(toUserResponse(user, token));
  }

  private UserResponse toUserResponse(User user) {
    return toUserResponse(user, null);
  }

  private UserResponse toUserResponse(User user, String token) {
    return UserResponse.builder()
        .id(user.getId())
        .firstname(Objects.nonNull(user.getFirstname()) ? user.getFirstname() : null)
        .lastname(Objects.nonNull(user.getLastname()) ? user.getLastname() : null)
        .username(user.getUsername())
        .email(user.getEmail())
        .token(Objects.nonNull(token) ? token : null)
        .phoneNumber(Objects.nonNull(user.getPhoneNumber()) ? user.getPhoneNumber() : null)
        .placeOfBirth(Objects.nonNull(user.getPlaceOfBirth()) ? user.getPlaceOfBirth() : null)
        .dateOfBirth(Objects.nonNull(user.getDateOfBirth()) ?
            dateUtil.toDateOnlyFormat(user.getDateOfBirth())  : null)
        .role(RoleResponse.builder()
            .name(user.getRole().getName())
            .roleType(user.getRole().getRoleType().name())
            .description(user.getRole().getDescription())
            .build())
        .qrCodeImageUrl(user.getQrCodeImage().getUrl())
        .build();
  }
}
