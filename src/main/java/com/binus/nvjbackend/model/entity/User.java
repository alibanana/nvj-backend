package com.binus.nvjbackend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = User.COLLECTION_NAME)
public class User extends BaseMongoEntity {

  public static final String COLLECTION_NAME = "users";

  private static final long serialVersionUID = 3161008033335897044L;

  private String username;
  private String email;
  private String password;

  private Role role;

  public User(String id, Date createdAt, Date updatedAt, String username, String email,
      String password, Role role) {
    super(id, createdAt, updatedAt);
    this.username = username;
    this.email = email;
    this.password = password;
    this.role = role;
  }
}
