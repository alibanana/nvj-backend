package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Image;
import com.binus.nvjbackend.model.entity.OnSiteExperience;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.OnSiteExperienceRepository;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceAddImageRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceUpdateRequest;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceUpdateThumbnailRequest;
import com.binus.nvjbackend.rest.web.util.OtherUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OnSiteExperienceServiceImpl implements OnSiteExperienceService {

  @Autowired
  private ImageService imageService;

  @Autowired
  private OnSiteExperienceRepository onSiteExperienceRepository;

  @Autowired
  private OtherUtil otherUtil;

  @Override
  public OnSiteExperience create(OnSiteExperienceRequest request) {
    validateOnSiteExperienceDoesNotExistsByTitle(request.getTitle());
    Image thumbnail = imageService.uploadImage(request.getThumbnail());
    List<Image> images = null;
    if (Objects.nonNull(request.getImages())) {
      images = request.getImages().stream()
          .map(file -> imageService.uploadImage(file))
          .collect(Collectors.toList());
    }
    return onSiteExperienceRepository.save(buildOnSiteExperience(request, thumbnail, images));
  }

  @Override
  public Page<OnSiteExperience> findByFilter(Integer page, Integer size, String orderBy,
      String sortBy, OnSiteExperienceFilterRequest request) {
    PageRequest pageRequest = otherUtil.validateAndGetPageRequest(page, size, orderBy, sortBy);
    return onSiteExperienceRepository.findAllByIdAndTitle(request.getId(), request.getTitle(),
        pageRequest);
  }

  @Override
  public List<OnSiteExperience> findAll() {
    return onSiteExperienceRepository.findAll();
  }

  @Override
  public List<OnSiteExperience> findAllWithSorting(String orderBy, String sortBy) {
    if (Objects.nonNull(orderBy) || Objects.nonNull(sortBy)) {
      otherUtil.validateSortByAndOrderBy(sortBy, orderBy);
      onSiteExperienceRepository.findAllWithSorting(Sort.by(sortBy, orderBy));
    }
    return onSiteExperienceRepository.findAll();
  }

  @Override
  public OnSiteExperience findById(String id) {
    OnSiteExperience onSiteExperience = Optional.of(onSiteExperienceRepository.findById(id))
        .get()
        .orElse(null);
    if (Objects.isNull(onSiteExperience)) {
      throw new BaseException(ErrorCode.ON_SITE_EXPERIENCE_NOT_FOUND);
    }
    return onSiteExperience;
  }

  @Override
  public OnSiteExperience updateById(String id, OnSiteExperienceUpdateRequest request) {
    OnSiteExperience onSiteExperience = findById(id);
    if (!onSiteExperience.getTitle().equals(request.getTitle())) {
      validateOnSiteExperienceDoesNotExistsByTitle(request.getTitle());
      onSiteExperience.setTitle(request.getTitle());
    }
    if (!onSiteExperience.getDescription().equals(request.getDescription())) {
      onSiteExperience.setDescription(request.getDescription());
    }
    return onSiteExperienceRepository.save(onSiteExperience);
  }

  @Override
  public OnSiteExperience updateThumbnailById(String id,
      OnSiteExperienceUpdateThumbnailRequest request) {
    OnSiteExperience onSiteExperience = findById(id);
    imageService.deleteById(onSiteExperience.getThumbnail().getId());
    Image thumbnail = imageService.uploadImage(request.getThumbnail());
    onSiteExperience.setThumbnail(thumbnail);
    return onSiteExperienceRepository.save(onSiteExperience);
  }

  @Override
  public OnSiteExperience removeImageByIdAndImageId(String id, String imageId) {
    OnSiteExperience onSiteExperience = findById(id);
    List<String> existingImageIdlist = onSiteExperience.getImages().stream()
        .map(Image::getId)
        .collect(Collectors.toList());
    if (!existingImageIdlist.contains(imageId)) {
      throw new BaseException(ErrorCode.IMAGE_NOT_FOUND);
    }
    for (int i = 0; i < onSiteExperience.getImages().size(); i++) {
      if (onSiteExperience.getImages().get(i).getId().equals(imageId)) {
        onSiteExperience.getImages().remove(i);
        imageService.deleteById(imageId);
        break;
      }
    }
    return onSiteExperienceRepository.save(onSiteExperience);
  }

  @Override
  public OnSiteExperience addImageById(String id, OnSiteExperienceAddImageRequest request) {
    OnSiteExperience onSiteExperience = findById(id);
    Image image = imageService.uploadImage(request.getImage());
    if (Objects.isNull(onSiteExperience.getImages())) {
      List<Image> images = new ArrayList<>();
      images.add(image);
      onSiteExperience.setImages(images);
    } else {
      onSiteExperience.getImages().add(image);
    }
    return onSiteExperienceRepository.save(onSiteExperience);
  }

  private void validateOnSiteExperienceDoesNotExistsByTitle(String title) {
     if (onSiteExperienceRepository.existsByTitle(title)) {
       throw new BaseException(ErrorCode.ON_SITE_EXPERIENCE_EXISTS);
     }
  }

  private OnSiteExperience buildOnSiteExperience(OnSiteExperienceRequest request, Image thumbnail,
      List<Image> images) {
    return OnSiteExperience.builder()
        .title(request.getTitle())
        .description(request.getDescription())
        .thumbnail(thumbnail)
        .images(images)
        .build();
  }
}
