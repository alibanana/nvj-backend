package com.binus.nvjbackend.rest.web.controller.server;

import com.binus.nvjbackend.model.entity.SystemParameter;
import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.request.systemparameter.SystemParameterKeyRequest;
import com.binus.nvjbackend.rest.web.model.request.systemparameter.SystemParameterRequest;
import com.binus.nvjbackend.rest.web.model.response.SystemParameterResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestListResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.SystemParameterService;
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

@Api(value = "System Parameters", description = "System Parameters Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_SYSTEM_PARAMETER)
public class SystemParameterController extends BaseController {

  @Autowired
  private SystemParameterService systemParameterService;

  @PostMapping(value = ApiPath.SYSTEM_PARAMETER_INSERT)
  public RestBaseResponse insert(@Valid @RequestBody SystemParameterRequest request) {
    systemParameterService.insert(request);
    return toBaseResponse();
  }

  @GetMapping
  public RestListResponse<SystemParameterResponse> findAll() {
    List<SystemParameter> systemParameterList = systemParameterService.findAll();
    return toListResponse(toSystemParameterListResponse(systemParameterList));
  }

  @PostMapping(value = ApiPath.SYSTEM_PARAMETER_FIND_VALUE_BY_KEY)
  public RestSingleResponse<Object> findValueByKey(
      @Valid @RequestBody SystemParameterKeyRequest request) {
    Object value = systemParameterService.findValueByKey(request.getKey());
    return toSingleResponse(value);
  }

  @PutMapping(value = ApiPath.SYSTEM_PARAMETER_UPDATE_BY_KEY)
  public RestBaseResponse updateByKey(@Valid @RequestBody SystemParameterRequest request) {
    systemParameterService.updateByKey(request);
    return toBaseResponse();
  }

  @DeleteMapping(value = ApiPath.SYSTEM_PARAMETER_DELETE_BY_KEY)
  public RestBaseResponse deleteByKey(@Valid @RequestBody SystemParameterKeyRequest request) {
    systemParameterService.deleteByKey(request.getKey());
    return toBaseResponse();
  }

  private List<SystemParameterResponse> toSystemParameterListResponse(
      List<SystemParameter> systemParameterList) {
    return systemParameterList.stream()
        .map(systemParamater -> SystemParameterResponse.builder()
            .key(systemParamater.getKey())
            .value(systemParamater.getValue())
            .description(systemParamater.getDescription())
            .type(systemParamater.getType())
            .build())
        .collect(Collectors.toList());
  }
}
