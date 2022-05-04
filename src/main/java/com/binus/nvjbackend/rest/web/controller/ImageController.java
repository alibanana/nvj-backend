package com.binus.nvjbackend.rest.web.controller;

import com.binus.nvjbackend.model.entity.Image;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.response.ImageResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.ImageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "Image", description = "Image Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_IMAGE)
public class ImageController extends BaseController {

  @Autowired
  private ImageService imageService;

  @PostMapping(value = ApiPath.UPLOAD_IMAGE)
  public RestSingleResponse<ImageResponse> uploadImage(@RequestParam("file")MultipartFile file) {
    Image image = imageService.uploadImage(file);
    return toSingleResponse(ImageResponse.builder()
        .name(image.getName())
        .url(image.getUrl())
        .build());
  }
}
