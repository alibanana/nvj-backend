package com.binus.nvjbackend.rest.web.model.request.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderFilterRequest implements Serializable {

  private static final long serialVersionUID = -1700886873516771266L;

  private String id;
  private String firstname;
  private String lastname;
  private String email;
  private String phoneNumber;

  @JsonFormat(pattern = "dd-MM-yyyy")
  private Date fromVisitDate;

  @JsonFormat(pattern = "dd-MM-yyyy")
  private Date toVisitDate;

  private String paymentType;
  private Boolean isManualOrder;
  private String midtransOrderId;
  private String midtransTransactionStatus;
}
