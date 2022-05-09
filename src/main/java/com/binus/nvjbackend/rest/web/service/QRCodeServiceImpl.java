package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.config.properties.SysparamProperties;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class QRCodeServiceImpl implements QRCodeService {

  @Autowired
  private SysparamProperties sysparamProperties;

  private QRCodeWriter qrCodeWriter;

  private Path path;

  @PostConstruct
  public void init() {
    qrCodeWriter = new QRCodeWriter();
    try {
      path = Paths.get(sysparamProperties.getFileStorageLocation());
      Files.createDirectories(path);
    } catch (Exception e) {
      throw new BaseException(ErrorCode.STORAGE_INITIALIZATION_ERROR);
    }
  }

  @Override
  public void generateQRCodeImage(String text, String filename) throws WriterException, IOException {
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE,
        sysparamProperties.getQrCodeDefaultWidth(), sysparamProperties.getQrCodeDefaultHeight());
    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path.resolve(filename));
  }
}
