package com.binus.nvjbackend.config;

import com.binus.nvjbackend.config.properties.MongoDbProperties;
import com.binus.nvjbackend.rest.web.service.UserDetailsImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
    basePackages = "com.binus.nvjbackend.repository",
    mongoTemplateRef = "NvjMongoTemplate")
@ConfigurationProperties(prefix = "nvj")
public class NVJMongoDbConfig extends MongoDbConfig {

  public NVJMongoDbConfig(MongoDbProperties mongoDbProperties) {
    super(mongoDbProperties);
  }

  @Override
  @Bean(name = "NvjMongoTemplate")
  public MongoTemplate mongoTemplate() throws Exception {
    return new MongoTemplate(mongoClient(), mongoProperties.getDatabase());
  }
}
