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
@Document(collection = User.COLLECTION_NAME)
public class User extends BaseMongoEntity {

  public static final String COLLECTION_NAME = "users";

  private static final long serialVersionUID = 3161008033335897044L;

  private String username;
  private String email;
  private String password;

  public User(String id, Date createdAt, Date updatedAt, String username, String email,
      String password) {
    super(id, createdAt, updatedAt);
    this.username = username;
    this.email = email;
    this.password = password;
  }

  @Override
  public String toString() {
    return String.format("User [username=%s, email=%s, password=%s, toString()=%s]",
        this.username, this.email, this.password, super.toString());
  }
}
