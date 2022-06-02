package com.binus.nvjbackend.rest.web.model.request.ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketRequest implements Serializable {

  private static final long serialVersionUID = -7921030218064858280L;

  @NotBlank
  private String title;

  @NotBlank
  private String description;

  @NotNull
  private Double price;

  @NotBlank
  private String phoneNumber;

  @NotBlank
  private String contactName;

  @NotNull
  private boolean purchasable;

  private boolean markForDelete;
}
