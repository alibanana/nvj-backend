package com.binus.nvjbackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = TicketArchive.COLLECTION_NAME)
public class TicketArchive extends BaseMongoEntity {

  public static final String COLLECTION_NAME = "ticket_archives";

  private static final long serialVersionUID = 8261656329925652895L;

  private String title;
  private String description;
  private Integer price;

  @Version
  private Integer version;

  private boolean markForDelete;
}
