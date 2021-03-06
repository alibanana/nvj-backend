package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageService {

  Image uploadImage(MultipartFile file);

  byte[] retrieveImage(String filename) throws IOException;

  void deleteImage(String filename);

  void deleteById(String id);

  Image validateAndStoreImageToMongo(Path path);
}
