package com.binus.nvjbackend.rest.web.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemResponse implements Serializable {

  private static final long serialVersionUID = -1402821524129061578L;

  private String id;
  private String ticketId;
  private String title;
  private Integer quantity;
  private Double price;
}
