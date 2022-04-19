package com.binus.nvjbackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Role.COLLECTION_NAME)
public class Role extends BaseMongoEntity {

  public static final String COLLECTION_NAME = "roles";

  private static final long serialVersionUID = -8335172345391215764L;

  private String title;
  private String description;
  private List<String> access;

  public Role(String id, Date createdAt, Date updatedAt, String title, String description,
      List<String> access) {
    super(id, createdAt, updatedAt);
    this.title = title;
    this.description = description;
    this.access = access;
  }

  @Override
  public String toString() {
    return String.format("Role [title=%s, description=%s, access=%s, toString()=%s]",
        this.title, this.description, this.access, super.toString());
  }
}
