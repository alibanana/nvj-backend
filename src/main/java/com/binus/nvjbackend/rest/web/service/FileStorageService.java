package com.binus.nvjbackend.rest.web.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface FileStorageService {

  List<Path> storeFiles(MultipartFile... files);

  void validateFileExistsByFilename(String filename);

  byte[] retrieveFile(String filename);
}
