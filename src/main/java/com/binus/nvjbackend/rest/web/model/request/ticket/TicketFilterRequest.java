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
public class TicketFilterRequest implements Serializable {

  private static final long serialVersionUID = 4227437180014518683L;

  private String title;
  private Integer fromPrice;
  private Integer toPrice;
}
