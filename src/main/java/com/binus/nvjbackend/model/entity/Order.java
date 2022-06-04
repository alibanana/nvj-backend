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
  private String description;
  private String phoneNumber;
  private Date visitDate;
  private Double totalPrice;
  private Midtrans midtrans;
  private String paymentType;
  private Boolean isManualOrder;

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

    // All variables below are not available initially
    private Date transactionTime;
    private String transactionStatus;
    private String transactionId;
    private String statusMessage;
    private String statusCode;
    private String signatureKey;
    private String paymentType;
    private String merchantId;
    private Date settlementTime;
    private Double grossAmount;
    private String fraudStatus;
    private String currency;

    // For Credit Cards
    private String maskedCard;
    private String eci;
    private String channelResponseMessage;
    private String channelResponseCode;
    private String cardType;
    private String bank;
    private String approvalCode;

    // For QRIS
    private String acquirer;

    // For Mandiri Bill
    private String billerCode;
    private String billKey;

    // For Virtual Accounts
    private String permataVaNumber;
    private List<VaNumber> vaNumbers;
    private List<PaymentAmount> paymentAmounts;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VaNumber {
      private String vaNumber;
      private String bank;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaymentAmount {
      private Date paidAt;
      private Double amount;
    }
  }
}
