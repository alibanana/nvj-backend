package com.binus.nvjbackend.rest.web.controller.server;

import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.response.StatisticGetWeeklyDataResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.StatisticService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Statistic", description = "Statistic Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_STATISTIC)
public class StatisticController extends BaseController {

  @Autowired
  private StatisticService statisticService;

  @PostMapping(value = ApiPath.STATISTIC_GET_WEEKLY_DATA)
  public RestSingleResponse<StatisticGetWeeklyDataResponse> getDailyData() {
    return toSingleResponse(statisticService.getWeeklyData());
  }
}
