package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.config.properties.SysparamProperties;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FileStorageServiceImpl implements FileStorageService {

  private Path rootDir;

  @Autowired
  SysparamProperties sysparamProperties;

  @PostConstruct
  public void init() {
    try {
      rootDir = Paths.get(sysparamProperties.getFileStorageLocation());
      Files.createDirectories(rootDir);
    } catch (Exception e) {
      throw new BaseException(ErrorCode.STORAGE_INITIALIZATION_ERROR);
    }
  }

  @Override
  public List<Path> storeFiles(MultipartFile... files) {
    validateFilesNotEmpty(files);
    return Arrays.stream(files)
        .map(this::validateAndStoreFile)
        .collect(Collectors.toList());
  }

  @Override
  public void validateFileExistsByFilename(String filename) {
    try {
      rootDir.resolve(filename);
    } catch (Exception e) {
      throw new BaseException(ErrorCode.FILE_NOT_FOUND_OR_UNREADABLE);
    }
  }

  @Override
  public byte[] retrieveFile(String filename) {
    try {
      return FileUtils.readFileToByteArray(
          new File(sysparamProperties.getFileStorageLocation() + filename));
    } catch (IOException e) {
      throw new BaseException(ErrorCode.FILE_NOT_FOUND_OR_UNREADABLE);
    }
  }

  private void validateFilesNotEmpty(MultipartFile... files) {
    if (files.length == 0) {
      throw new BaseException(ErrorCode.INVALID_REQUEST_PAYLOAD);
    }
  }

  private Path validateAndStoreFile(MultipartFile file) {
    String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    validateFileNotEmpty(file);
    validateFileName(filename);
    validateFileDoesntExists(filename);
    return storeFile(file, filename);
  }

  private void validateFileNotEmpty(MultipartFile file) {
    if (file.isEmpty()) throw new BaseException(ErrorCode.FILE_IS_EMPTY);
  }

  private void validateFileName(String filename) {
    if (filename.contains("..")) throw new BaseException(ErrorCode.FILENAME_INVALID);
  }

  private void validateFileDoesntExists(String filename) {
    if (Files.exists(Paths.get(sysparamProperties.getFileStorageLocation() + filename)))
      throw new BaseException(ErrorCode.FILE_ALREADY_EXISTS);
  }

  private Path storeFile(MultipartFile file, String filename) {
    try (InputStream inputStream = file.getInputStream()) {
      createDirectoriesIfNotExists(rootDir);
      Files.copy(inputStream, rootDir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
      return rootDir.resolve(filename);
    } catch (IOException e) {
      throw new BaseException(ErrorCode.FAILED_STORING_FILE);
    }
  }

  private void createDirectoriesIfNotExists(Path directory) throws IOException {
    if (Files.notExists(rootDir)) Files.createDirectories(rootDir);
  }
}
