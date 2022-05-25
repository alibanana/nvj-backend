package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Image;
import com.binus.nvjbackend.model.entity.OnSiteExperience;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.OnSiteExperienceRepository;
import com.binus.nvjbackend.rest.web.model.request.onsiteexperience.OnSiteExperienceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  @Override
  public OnSiteExperience create(OnSiteExperienceRequest request) {
    validateOnSiteExperienceDoesNotExistsByTitle(request.getTitle());
    Image thumbnail = imageService.uploadImage(request.getThumbnail());
    List<Image> images = request.getImages().stream()
        .map(file -> imageService.uploadImage(file))
        .collect(Collectors.toList());
    return onSiteExperienceRepository.save(buildOnSiteExperience(request, thumbnail, images));
  }

  @Override
  public List<OnSiteExperience> findAll() {
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
