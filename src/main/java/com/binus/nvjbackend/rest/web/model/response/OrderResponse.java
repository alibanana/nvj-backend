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
public class OrderResponse implements Serializable {

  private static final long serialVersionUID = 9067294094910528286L;

  private String id;
  private String firstname;
  private String lastname;
  private String email;
  private String description;
  private String phoneNumber;
  private Date visitDate;
  private Double totalPrice;
  private List<OrderItemResponse> orderItems;
}
