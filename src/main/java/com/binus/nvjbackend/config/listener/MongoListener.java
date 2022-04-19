package com.binus.nvjbackend.config.listener;

import com.binus.nvjbackend.model.entity.BaseMongoEntity;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class MongoListener extends AbstractMongoEventListener<BaseMongoEntity> {

  @Override
  public void onBeforeConvert(BeforeConvertEvent<BaseMongoEntity> event) {
    super.onBeforeConvert(event);
    Date dateNow = new Date();
    event.getSource().setUpdatedAt(dateNow);
    if (Objects.isNull(event.getSource().getCreatedAt())) {
      event.getSource().setCreatedAt(dateNow);
    }
  }
}
