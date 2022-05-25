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
@Document(collection = OnSiteExperience.COLLECTION_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OnSiteExperience extends BaseMongoEntity {

  public static final String COLLECTION_NAME = "on_site_experiences";

  private static final long serialVersionUID = -2237016057946362513L;

  private String title;
  private String description;

  @DBRef
  private Image thumbnail;

  @DBRef
  private List<Image> images;

  public OnSiteExperience(String id, Date createdAt, Date updatedAt, String title,
      String description, Image thumbnail, List<Image> images) {
    super(id, createdAt, updatedAt);
    this.title = title;
    this.description = description;
    this.thumbnail = thumbnail;
    this.images = images;
  }
}
