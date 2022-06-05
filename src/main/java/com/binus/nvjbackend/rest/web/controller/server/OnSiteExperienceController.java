package com.binus.nvjbackend.rest.web.controller.server;

import com.binus.nvjbackend.model.entity.Image;
import com.binus.nvjbackend.model.entity.OnSiteExperience;
import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceRequest;
import com.binus.nvjbackend.rest.web.model.response.OnSiteExperienceResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestListResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestPageResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.OnSiteExperienceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
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

  @PostMapping(value = ApiPath.EXPERIENCE_FIND_ALL)
  public RestListResponse<OnSiteExperienceResponse> findAll(
      @RequestParam(required = false) String orderBy,
      @RequestParam(required = false) String sortBy) {
    List<OnSiteExperience> onSiteExperiences = onSiteExperienceService.findAllWithSorting(orderBy,
        sortBy);
    return toListResponse(onSiteExperiences.stream()
        .map(this::toOnSiteExperienceResponse)
        .collect(Collectors.toList()));
  }

  @PostMapping(value = ApiPath.EXPERIENCE_FIND_BY_FILTER)
  public RestPageResponse<OnSiteExperienceResponse> findByFilter(
      @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
      @RequestParam(required = false) String orderBy, @RequestParam(required = false) String sortBy,
      @Valid @RequestBody OnSiteExperienceFilterRequest request) {
    Page<OnSiteExperience> onSiteExperiences = onSiteExperienceService.findByFilter(page, size,
        orderBy, sortBy, request);
    List<OnSiteExperienceResponse> content = onSiteExperiences.getContent().stream()
        .map(this::toOnSiteExperienceResponse)
        .collect(Collectors.toList());
    return toPageResponse(content, onSiteExperiences);
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
