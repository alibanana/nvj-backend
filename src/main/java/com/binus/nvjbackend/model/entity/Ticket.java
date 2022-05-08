package com.binus.nvjbackend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Ticket.COLLECTION_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket extends BaseMongoEntity {

  public static final String COLLECTION_NAME = "tickets";

  private static final long serialVersionUID = 8261656329925652895L;

  private String title;
  private String description;
  private Integer price;

  @Version
  private Integer version;

  private boolean markForDelete;

  @DBRef
  private List<TicketArchive> ticketArchives;

  public Ticket(String id, Date createdAt, Date updatedAt, String title, String description,
      Integer price, Integer version, boolean markForDelete, List<TicketArchive> ticketArchives) {
    super(id, createdAt, updatedAt);
    this.title = title;
    this.description = description;
    this.price = price;
    this.version = version;
    this.markForDelete = markForDelete;
    this.ticketArchives = ticketArchives;
  }
}
