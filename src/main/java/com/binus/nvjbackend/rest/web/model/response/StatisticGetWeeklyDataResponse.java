package com.binus.nvjbackend.rest.web.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticGetWeeklyDataResponse implements Serializable {

  private static final long serialVersionUID = 8173589943656875917L;

  private Order order;
  private Ticket ticket;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Order {
    private List<Double> dayToDayOrderValues;
    private List<Integer> dayToDayOrderCount;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Ticket {
    private List<Integer> datToDayTicketSalesCount;
  }
}
