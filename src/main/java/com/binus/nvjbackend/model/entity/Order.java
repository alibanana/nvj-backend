package com.binus.nvjbackend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Order.COLLECTION_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order extends BaseMongoEntity {

  public static final String COLLECTION_NAME = "orders";

  private static final long serialVersionUID = 5280232148800876783L;

  private String firstname;
  private String lastname;
  private String email;
  private Date visitDate;
  private Integer totalPrice;
  private Midtrans midtrans;

  @DBRef
  private List<OrderItem> orderItems;

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Midtrans {
    private String orderId;
    private String token;
    private String redirectUrl;

    // Not available initially
    private Date transactionTime;
    private String transactionStatus;
    private String transactionId;
    private Date settlementTime;
    private String paymentType;
    private Double grossAmount;
    private String fraudStatus;
    private String currency;
  }
}
