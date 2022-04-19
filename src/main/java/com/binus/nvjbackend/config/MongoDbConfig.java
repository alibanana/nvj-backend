package com.binus.nvjbackend.config;

import com.binus.nvjbackend.config.properties.MongoDbProperties;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.concurrent.TimeUnit;

@Data
@Configuration
@RequiredArgsConstructor
public abstract class MongoDbConfig {

  private final MongoDbProperties mongoDbProperties;

  protected MongoProperties mongoProperties;

  public MongoClient mongoClient() {
    MongoCredential credential =
        MongoCredential.createScramSha1Credential(mongoProperties.getUsername(),
            mongoProperties.getAuthenticationDatabase(), mongoProperties.getPassword());

    MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
        .readPreference(ReadPreference.valueOf(mongoDbProperties.getReadPreference()))
        .writeConcern(WriteConcern.valueOf(mongoDbProperties.getWriteConcern()))
        .applyToConnectionPoolSettings(
            builder -> builder.minSize(mongoDbProperties.getMinConnectionsPerHost())
                .maxSize(mongoDbProperties.getConnectionPerHost())
                .maxConnectionIdleTime(mongoDbProperties.getMaxConnectionIdleTime(), TimeUnit.MILLISECONDS)
                .maxConnectionLifeTime(mongoDbProperties.getMaxConnectionLifeTime(), TimeUnit.MILLISECONDS)
                .maxWaitTime(mongoDbProperties.getMaxWaitTime(), TimeUnit.MILLISECONDS))
        .applyConnectionString(new ConnectionString(mongoProperties.getUri()))
        .applyToSocketSettings(builder -> builder
            .connectTimeout(mongoDbProperties.getSocketTimeout(), TimeUnit.MILLISECONDS)
            .readTimeout(mongoDbProperties.getSocketTimeout(), TimeUnit.MILLISECONDS))
        .applyToServerSettings(builder -> builder
            .heartbeatFrequency(mongoDbProperties.getHeartbeatFrequency(), TimeUnit.MILLISECONDS)
            .minHeartbeatFrequency(mongoDbProperties.getMinHeartbeatFrequency(),
                TimeUnit.MILLISECONDS))
        .credential(credential)
        .build();

    return MongoClients.create(mongoClientSettings);
  }

  abstract public MongoTemplate mongoTemplate() throws Exception;
}
