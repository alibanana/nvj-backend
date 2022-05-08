package com.binus.nvjbackend.model.entity;

import com.binus.nvjbackend.model.enums.RoleType;
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
@Document(collection = Role.COLLECTION_NAME)
public class Role extends BaseMongoEntity {

  public static final String COLLECTION_NAME = "roles";

  private static final long serialVersionUID = -2820808057421084274L;

  private String name;
  private RoleType roleType;
  private String description;

  public Role(String id, Date createdAt, Date updatedAt, String name, RoleType roleType,
      String description) {
    super(id, createdAt, updatedAt);
    this.name = name;
    this.roleType = roleType;
    this.description = description;
  }
}
