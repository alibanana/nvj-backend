package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.config.properties.SysparamProperties;
import com.binus.nvjbackend.model.entity.Image;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.enums.FileTypes;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

  @Autowired
  private FileStorageService fileStorageService;

  @Autowired
  private SysparamProperties sysparamProperties;

  @Autowired
  private ImageRepository imageRepository;

  @Override
  public Image uploadImage(MultipartFile file) {
    String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    validateFileTypeFromFileName(filename);
    return fileStorageService.storeFiles(file).stream()
        .map(this::validateAndStoreImageToMongo)
        .collect(Collectors.toList())
        .get(0);
  }

  @Override
  public byte[] retrieveImage(String filename) throws IOException {
    validateFileTypeFromFileName(filename);
    fileStorageService.validateFileExistsByFilename(filename);
    return fileStorageService.retrieveFile(filename);
  }

  @Override
  public void deleteImage(String filename) {
    fileStorageService.validateFileExistsByFilename(filename);
    fileStorageService.removeFile(filename);
    imageRepository.deleteByName(filename);
  }

  @Override
  public void deleteById(String id) {
    Image image = Optional.of(imageRepository.findById(id)).get()
        .orElse(null);
    if (Objects.isNull(image)) {
      throw new BaseException(ErrorCode.IMAGE_NOT_FOUND);
    }
    fileStorageService.validateFileExistsByFilename(image.getName());
    fileStorageService.removeFile(image.getName());
    imageRepository.deleteById(id);
  }

  @Override
  public Image validateAndStoreImageToMongo(Path path) {
    String name = path.getFileName().toString();
    fileStorageService.validateFileExistsByFilename(name);
    String url = sysparamProperties.getFileRetrieveUrl() + name;
    return storeImageToMongo(name, url);
  }

  private void validateFileTypeFromFileName(String filename) {
    String mimetype = URLConnection.guessContentTypeFromName(filename);
    if (!mimetype.equals(FileTypes.IMAGE_PNG.getType()) &&
        !mimetype.equals(FileTypes.IMAGE_JPEG.getType())) {
      throw new BaseException(ErrorCode.FILETYPE_MUST_BE_IMAGE);
    }
  }

  private Image storeImageToMongo(String filename, String url) {
    try {
      Image image = imageRepository.findByName(filename);
      return Objects.nonNull(image) ? image :
          imageRepository.save(
              Image.builder()
                  .name(filename)
                  .url(url)
                  .build());
    } catch (Exception e) {
      throw new BaseException(ErrorCode.FAILED_STORING_IMAGE);
    }
  }
}
