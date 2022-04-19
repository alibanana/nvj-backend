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
@Document(collection = SystemParameter.COLLECTION_NAME)
public class SystemParameter extends BaseMongoEntity {

  public static final String COLLECTION_NAME = "system_parameters";

  private static final long serialVersionUID = -449298820381898580L;

  private String key;
  private String value;
  private String description;
  private String type;

  public SystemParameter(String id, Date createdAt, Date updatedAt, String key, String value,
      String description, String type) {
    super(id, createdAt, updatedAt);
    this.key = key;
    this.value = value;
    this.description = description;
    this.type = type;
  }

  @Override
  public String toString() {
    return String.format("SystemParameter [key=%s, value=%s, description=%s, type=%s, toString()=%s]",
        this.key, this.value, this.description, this.type, super.toString());
  }
}
