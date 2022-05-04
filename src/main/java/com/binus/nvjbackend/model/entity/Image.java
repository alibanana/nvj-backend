package com.binus.nvjbackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Image.COLLECTION_NAME)
public class Image extends BaseMongoEntity {

  public static final String COLLECTION_NAME = "images";

  private static final long serialVersionUID = -8508746519948686597L;

  private String name;
  private String url;

  public Image(String id, Date createdAt, Date updatedAt, String name, String url) {
    super(id, createdAt, updatedAt);
    this.name = name;
    this.url = url;
  }

  @Override
  public String toString() {
    return String.format("Image [name=%s, url=%s, toString()=%s]",
        this.name, this.url, super.toString());
  }
}
