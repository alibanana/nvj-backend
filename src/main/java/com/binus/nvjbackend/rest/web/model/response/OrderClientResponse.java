package com.binus.nvjbackend.rest.web.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderClientResponse implements Serializable {

  private static final long serialVersionUID = -5346318577562716354L;

  private String id;
  private String firstname;
  private String lastname;
  private String email;
  private String phoneNumber;
  private Date visitDate;
  private Double totalPrice;
  private List<OrderItemClientResponse> orderItems;
  private Midtrans midtrans;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Midtrans {
    private String orderId;
    private String token;
    private String redirectUrl;
    private String transactionStatus;
  }
}
