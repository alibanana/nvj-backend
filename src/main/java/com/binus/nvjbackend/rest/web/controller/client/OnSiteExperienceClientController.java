package com.binus.nvjbackend.rest.web.controller.client;

import com.binus.nvjbackend.model.entity.Image;
import com.binus.nvjbackend.model.entity.OnSiteExperience;
import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPathClient;
import com.binus.nvjbackend.rest.web.model.response.OnSiteExperienceClientResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestListResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.OnSiteExperienceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Client - Experiences", description = "Client - Experiences Service API")
@RestController
@RequestMapping(value = ApiPathClient.BASE_PATH_EXPERIENCE)
@Validated
public class OnSiteExperienceClientController extends BaseController {

  @Autowired
  private OnSiteExperienceService onSiteExperienceService;

  @PostMapping(value = ApiPathClient.EXPERIENCE_FIND_ALL)
  public RestListResponse<OnSiteExperienceClientResponse> findAll() {
    List<OnSiteExperience> onSiteExperiences = onSiteExperienceService.findAll();
    return toListResponse(onSiteExperiences.stream()
        .map(this::toOnSiteExperienceClientResponse)
        .collect(Collectors.toList()));
  }

  @PostMapping(value = ApiPathClient.EXPERIENCE_FIND_BY_ID)
  public RestSingleResponse<OnSiteExperienceClientResponse> findById(
      @RequestParam @NotBlank String id) {
    OnSiteExperience onSiteExperience = onSiteExperienceService.findById(id);
    return toSingleResponse(toOnSiteExperienceClientResponse(onSiteExperience));
  }

  private OnSiteExperienceClientResponse toOnSiteExperienceClientResponse(
      OnSiteExperience onSiteExperience) {
    return OnSiteExperienceClientResponse.builder()
        .id(onSiteExperience.getId())
        .title(onSiteExperience.getTitle())
        .description(onSiteExperience.getDescription())
        .thumbnail(onSiteExperience.getThumbnail().getUrl())
        .images(onSiteExperience.getImages().stream()
            .map(Image::getUrl)
            .collect(Collectors.toList()))
        .build();
  }
}
