package com.binus.nvjbackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.GeneratedValue;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseMongoEntity implements Serializable {

  public static final String ID = "ID";

  public static final String CREATED_AT = "created_at";

  public static final String UPDATED_AT = "updated_at";

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid2")
  private String id;

  @Field(value = BaseMongoEntity.CREATED_AT)
  private Date createdAt;

  @Field(value = BaseMongoEntity.UPDATED_AT)
  private Date updatedAt;

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("BaseMongoEntity{");
    sb.append("id='").append(id).append('\'');
    sb.append(", createdAt=").append(createdAt);
    sb.append(", updatedAt=").append(updatedAt);
    sb.append('}');
    return sb.toString();
  }
}
