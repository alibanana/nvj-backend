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
@Document(collection = EmailTemplate.COLLECTION_NAME)
public class EmailTemplate extends BaseMongoEntity {

  public static final String COLLECTION_NAME = "email_templates";

  private static final long serialVersionUID = 2253982960431026667L;

  private String templateName;
  private String from;
  private String subject;
  private String content;

  public EmailTemplate(String id, Date createdAt, Date updatedAt, String templateName, String from,
      String subject, String content) {
    super(id, createdAt, updatedAt);
    this.templateName = templateName;
    this.from = from;
    this.subject = subject;
    this.content = content;
  }
}
