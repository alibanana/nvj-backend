package com.binus.nvjbackend.rest.web.controller.server;

import com.binus.nvjbackend.model.entity.Image;
import com.binus.nvjbackend.model.entity.OnSiteExperience;
import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceRequest;
import com.binus.nvjbackend.rest.web.model.response.OnSiteExperienceResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.OnSiteExperienceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(value = "Experiences", description = "Experiences Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_EXPERIENCE)
public class OnSiteExperienceController extends BaseController {

  @Autowired
  private OnSiteExperienceService onSiteExperienceService;

  @PostMapping(value = ApiPath.EXPERIENCE_CREATE)
  public RestSingleResponse<OnSiteExperienceResponse> create(
      @Valid @ModelAttribute OnSiteExperienceRequest request) {
    OnSiteExperience onSiteExperience = onSiteExperienceService.create(request);
    return toSingleResponse(toOnSiteExperienceResponse(onSiteExperience));
  }

  private OnSiteExperienceResponse toOnSiteExperienceResponse(OnSiteExperience onSiteExperience) {
    return OnSiteExperienceResponse.builder()
        .id(onSiteExperience.getId())
        .title(onSiteExperience.getTitle())
        .description(onSiteExperience.getDescription())
        .thumbnail(onSiteExperience.getThumbnail().getUrl())
        .images(Objects.nonNull(onSiteExperience.getImages()) ? onSiteExperience.getImages()
            .stream()
            .map(Image::getUrl)
            .collect(Collectors.toList()) : null)
        .build();
  }
}
