package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.SystemParameter;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.enums.SystemParameterType;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.SystemParameterRepository;
import com.binus.nvjbackend.rest.web.model.request.systemparameter.SystemParameterRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class SystemParameterServiceImpl implements SystemParameterService {

  @Autowired
  private SystemParameterRepository systemParameterRepository;

  @Override
  public void insert(SystemParameterRequest request) {
    validateSystemParameterRequest(request);
    SystemParameter systemParameter = new SystemParameter();
    if (systemParameterRepository.existsByKey(request.getKey())) {
      throw new BaseException(ErrorCode.SYSPARAM_KEY_ALREADY_EXISTS);
    }
    BeanUtils.copyProperties(request, systemParameter);
    systemParameterRepository.save(systemParameter);
  }

  @Override
  public List<SystemParameter> findAll() {
    return systemParameterRepository.findAll();
  }

  @Override
  public Object findValueByKey(String key) {
    SystemParameter systemParameter = systemParameterRepository.findByKey(key);
    if (Objects.isNull(systemParameter)) {
      throw new BaseException(ErrorCode.SYSPARAM_KEY_NOT_FOUND);
    }
    return convertToValue(systemParameter);
  }

  @Override
  public void updateByKey(SystemParameterRequest request) {
    validateSystemParameterRequest(request);
    SystemParameter systemParameter = systemParameterRepository.findByKey(request.getKey());
    if (Objects.isNull(systemParameter)) {
      throw new BaseException(ErrorCode.SYSPARAM_KEY_NOT_FOUND);
    }
    updateSystemParameterWithRequestValue(systemParameter, request);
    systemParameterRepository.save(systemParameter);
  }

  @Override
  public void deleteByKey(String key) {
    if (!systemParameterRepository.existsByKey(key)) {
      throw new BaseException(ErrorCode.SYSPARAM_KEY_NOT_FOUND);
    }
    systemParameterRepository.deleteByKey(key);
  }

  private void validateSystemParameterRequest(SystemParameterRequest request) {
    if (!SystemParameterType.getAllNames().contains(request.getType())) {
      throw new BaseException(ErrorCode.SYSPARAM_TYPE_INVALID);
    }
  }

  private void updateSystemParameterWithRequestValue(SystemParameter systemParameter,
      SystemParameterRequest request) {
    systemParameter.setValue(request.getValue());
    systemParameter.setDescription(request.getDescription());
    systemParameter.setType(request.getType());
  }

  private Object convertToValue(SystemParameter systemParameter) {
    if (systemParameter.getType().equals(SystemParameterType.STRING.name())) {
      return systemParameter.getValue();
    } else if (systemParameter.getType().equals(SystemParameterType.INTEGER.name())) {
      return Integer.valueOf(systemParameter.getValue());
    } else if (systemParameter.getType().equals(SystemParameterType.ARRAY_STRING.name())) {
      return systemParameter.getValue().split(",");
    }
    return null;
  }
}
