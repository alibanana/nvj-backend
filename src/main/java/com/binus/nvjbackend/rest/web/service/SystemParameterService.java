package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.SystemParameter;
import com.binus.nvjbackend.rest.web.model.request.systemparameter.SystemParameterRequest;

import java.util.List;

public interface SystemParameterService {

  void insert(SystemParameterRequest request);

  List<SystemParameter> findAll();

  Object findValueByKey(String key);

  void updateByKey(SystemParameterRequest request);

  void deleteByKey(String key);
}
