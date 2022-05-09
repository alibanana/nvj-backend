package com.binus.nvjbackend.rest.web.service;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface QRCodeService {

  void generateQRCodeImage(String text, String filename) throws WriterException, IOException;
}
