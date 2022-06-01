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
public class TicketClientResponse implements Serializable {

  private static final long serialVersionUID = 6430846672835846026L;

  private String id;
  private String title;
  private Integer qty;
  private Integer price;
  private Boolean purchasable;
}
