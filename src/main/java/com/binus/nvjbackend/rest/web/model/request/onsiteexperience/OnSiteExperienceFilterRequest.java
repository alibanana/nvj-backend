package com.binus.nvjbackend.rest.web.model.request.onsiteexperience;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OnSiteExperienceFilterRequest implements Serializable {

  private static final long serialVersionUID = 936130394560497787L;

  private String id;
  private String title;
}
