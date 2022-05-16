package com.binus.nvjbackend.repository;

import com.binus.nvjbackend.model.entity.EmailTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailTemplateRepository extends MongoRepository<EmailTemplate, String> {

  boolean existsByTemplateName(String templateName);

  EmailTemplate findByTemplateName(String templateName);

  void deleteByTemplateName(String templateName);
}
