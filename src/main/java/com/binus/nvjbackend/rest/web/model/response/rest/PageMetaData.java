package com.binus.nvjbackend.rest.web.model.response.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class PageMetaData implements Serializable {

  private static final long serialVersionUID = -1359884237154863549L;

  private long pageSize = 0L;
  private long pageNumber = 0L;
  private long totalRecords = 0L;
  private long totalPages = 0L;
}
