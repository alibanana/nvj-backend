package com.binus.nvjbackend.rest.web.model.request.onsiteexperience;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OnSiteExperienceUpdateThumbnailRequest implements Serializable {

  private static final long serialVersionUID = -4630051608096049149L;

  @NotNull
  private MultipartFile thumbnail;
}
