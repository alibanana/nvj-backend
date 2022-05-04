package com.binus.nvjbackend.model.enums;

public enum FileTypes {

  IMAGE_PNG("image/png"),
  IMAGE_JPEG("image/jpeg");

  private final String type;

  FileTypes(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
