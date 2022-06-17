package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.rest.web.model.response.StatisticGetWeeklyDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatisticServiceImpl implements StatisticService {

  @Autowired
  private OrderService orderService;

  @Override
  public StatisticGetWeeklyDataResponse getWeeklyData() {
    Map<String, Object> weeklyOrderData = orderService.getWeeklyOrderData();
    return StatisticGetWeeklyDataResponse.builder()
        .order(StatisticGetWeeklyDataResponse.Order.builder()
            .dayToDayOrderValues((List<Double>) weeklyOrderData.get("orderValues"))
            .dayToDayOrderCount((List<Integer>) weeklyOrderData.get("orderCounts"))
            .build())
        .ticket(StatisticGetWeeklyDataResponse.Ticket.builder()
            .datToDayTicketSalesCount((List<Integer>) weeklyOrderData.get("ticketCounts"))
            .build())
        .build();
  }
}
