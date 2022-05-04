package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

  Image uploadImage(MultipartFile file);
}
