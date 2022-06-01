package com.binus.nvjbackend.rest.web.model.request.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest implements Serializable {

  private static final long serialVersionUID = 2453431216915945324L;

  @NotBlank
  private String firstname;

  @NotBlank
  private String lastname;

  @NotBlank
  private String email;

  @NotNull
  private Date visitDate;

  @NotEmpty
  private List<OrderItemRequest> orderItems;
}
