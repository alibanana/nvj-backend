package com.binus.nvjbackend.rest.web.controller.server;

import com.binus.nvjbackend.model.entity.Image;
import com.binus.nvjbackend.rest.web.controller.BaseController;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.response.ImageResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.ImageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;

@Api(value = "Image", description = "Image Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_IMAGE)
public class ImageController extends BaseController {

  @Autowired
  private ImageService imageService;

  @PostMapping(value = ApiPath.UPLOAD_IMAGE)
  public RestSingleResponse<ImageResponse> uploadImage(@RequestParam("file") MultipartFile file) {
    Image image = imageService.uploadImage(file);
    return toSingleResponse(ImageResponse.builder()
        .name(image.getName())
        .url(image.getUrl())
        .build());
  }

  @GetMapping(value = ApiPath.GET_IMAGE_BY_FILENAME)
  public ResponseEntity<byte[]> getImage(@PathVariable("filename") String filename)
      throws IOException {
    byte[] image = imageService.retrieveImage(filename);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(getFileTypeFromFileName(filename)))
        .body(image);
  }

  @DeleteMapping(value = ApiPath.DELETE_IMAGE_BY_FILENAME)
  public RestBaseResponse deleteImage(@PathVariable("filename") String filename) {
    imageService.deleteImage(filename);
    return toBaseResponse();
  }

  private String getFileTypeFromFileName(String filename) {
    return URLConnection.guessContentTypeFromName(filename);
  }
}
