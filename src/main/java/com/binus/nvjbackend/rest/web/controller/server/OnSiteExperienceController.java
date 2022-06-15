package com.binus.nvjbackend.rest.web.controller.server;

import com.binus.nvjbackend.model.entity.Image;
import com.binus.nvjbackend.model.entity.OnSiteExperience;
import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceAddImageRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceUpdateRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceUpdateThumbnailRequest;
import com.binus.nvjbackend.rest.web.model.response.OnSiteExperienceImageResponse;
import com.binus.nvjbackend.rest.web.model.response.OnSiteExperienceResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestListResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestPageResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.OnSiteExperienceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(value = "Experiences", description = "Experiences Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_EXPERIENCE)
@Validated
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

  @PutMapping(value = ApiPath.EXPERIENCE_UPDATE_BY_ID)
  public RestSingleResponse<OnSiteExperienceResponse> updateById(@RequestParam @NotBlank String id,
      @RequestBody OnSiteExperienceUpdateRequest request) {
    OnSiteExperience onSiteExperience = onSiteExperienceService.updateById(id, request);
    return toSingleResponse(toOnSiteExperienceResponse(onSiteExperience));
  }

  @PutMapping(value = ApiPath.EXPERIENCE_UPDATE_THUMBNAIL_BY_ID)
  public RestSingleResponse<OnSiteExperienceResponse> updateThumbnailById(
      @RequestParam @NotBlank String id,
      @Valid @ModelAttribute OnSiteExperienceUpdateThumbnailRequest request) {
    OnSiteExperience onSiteExperience = onSiteExperienceService.updateThumbnailById(id, request);
    return toSingleResponse(toOnSiteExperienceResponse(onSiteExperience));
  }

  @DeleteMapping(value = ApiPath.EXPERIENCE_REMOVE_IMAGE_BY_ID_AND_IMAGE_ID)
  public RestSingleResponse<OnSiteExperienceResponse> removeImageByIdAndImageId(
      @RequestParam @NotBlank String id, @RequestParam @NotBlank String imageId) {
    OnSiteExperience onSiteExperience =
        onSiteExperienceService.removeImageByIdAndImageId(id, imageId);
    return toSingleResponse(toOnSiteExperienceResponse(onSiteExperience));
  }

  @PostMapping(value = ApiPath.EXPERIENCE_ADD_IMAGE_BY_ID)
  public RestSingleResponse<OnSiteExperienceResponse> addImageById(@RequestParam @NotBlank String id,
      @Valid @ModelAttribute OnSiteExperienceAddImageRequest request) {
    OnSiteExperience onSiteExperience = onSiteExperienceService.addImageById(id, request);
    return toSingleResponse(toOnSiteExperienceResponse(onSiteExperience));
  }

  @DeleteMapping(value = ApiPath.EXPERIENCE_DELETE_BY_ID)
  public RestBaseResponse deleteById(@RequestParam @NotBlank String id) {
    onSiteExperienceService.deleteById(id);
    return toBaseResponse();
  }

  private OnSiteExperienceResponse toOnSiteExperienceResponse(OnSiteExperience onSiteExperience) {
    return OnSiteExperienceResponse.builder()
        .id(onSiteExperience.getId())
        .title(onSiteExperience.getTitle())
        .description(onSiteExperience.getDescription())
        .thumbnail(toOnSiteExperienceImageResponse(onSiteExperience.getThumbnail()))
        .images(Objects.nonNull(onSiteExperience.getImages()) ? onSiteExperience.getImages()
            .stream()
            .map(this::toOnSiteExperienceImageResponse)
            .collect(Collectors.toList()) : null)
        .build();
  }

  private OnSiteExperienceImageResponse toOnSiteExperienceImageResponse(Image image) {
    return OnSiteExperienceImageResponse.builder()
        .id(image.getId())
        .url(image.getUrl())
        .build();
  }
}
