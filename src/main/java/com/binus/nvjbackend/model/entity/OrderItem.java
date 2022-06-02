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

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = OrderItem.COLLECTION_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItem extends BaseMongoEntity {

  public static final String COLLECTION_NAME = "order_items";

  private static final long serialVersionUID = -7473046603854374337L;

  private Integer quantity;
  private Double price;

  @DBRef
  private Ticket ticket;

  private Integer ticketVersion;

  public OrderItem(String id, Date createdAt, Date updatedAt, Integer quantity, Double price,
      Ticket ticket, Integer ticketVersion) {
    super(id, createdAt, updatedAt);
    this.quantity = quantity;
    this.price = price;
    this.ticket = ticket;
    this.ticketVersion = ticketVersion;
  }
}
