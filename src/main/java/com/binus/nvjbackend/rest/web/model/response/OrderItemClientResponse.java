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
public class OrderItemClientResponse implements Serializable {

  private static final long serialVersionUID = -8850569273505710805L;

  private String id;
  private String title;
  private String description;
  private Integer quantity;
  private Double price;
}
