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
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = User.COLLECTION_NAME)
public class User extends BaseMongoEntity {

  public static final String COLLECTION_NAME = "users";

  private static final long serialVersionUID = 3161008033335897044L;

  private String firstname;
  private String lastname;
  private String username;
  private String email;
  private String password;
  private String phoneNumber;
  private String placeOfBirth;
  private Date dateOfBirth;

  @DBRef
  private Role role;

  @DBRef
  private Image qrCodeImage;

  public User(String id, Date createdAt, Date updatedAt, String firstname, String lastname,
      String username, String email, String password, String phoneNumber, String placeOfBirth,
      Date dateOfBirth, Role role, Image qrCodeImage) {
    super(id, createdAt, updatedAt);
    this.firstname = firstname;
    this.lastname = lastname;
    this.username = username;
    this.email = email;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.placeOfBirth = placeOfBirth;
    this.dateOfBirth = dateOfBirth;
    this.role = role;
    this.qrCodeImage = qrCodeImage;
  }
}
