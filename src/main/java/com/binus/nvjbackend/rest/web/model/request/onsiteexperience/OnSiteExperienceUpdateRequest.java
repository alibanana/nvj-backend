package com.binus.nvjbackend.rest.web.model.request.onsiteexperience;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OnSiteExperienceUpdateRequest implements Serializable {

  private static final long serialVersionUID = -8399086238909662445L;

  @NotBlank
  private String title;

  @NotBlank
  private String description;
}
