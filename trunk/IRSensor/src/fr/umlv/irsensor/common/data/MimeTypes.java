package fr.umlv.irsensor.common.data;

public enum MimeTypes {
  IMAGE_GIF(0, "image", "gif"),
  IMAGE_PNG(1, "image", "png"),
  IMAGE_JPG(3, "image", "jpg"),
  IMAGE_TIFF(4, "image", "tiff"),
  IMAGE_BMP(5, "image", "bmp"),
  TEXT(6, "text", "txt");

  private final int id;

  private final String name;

  private final String fileExtension;

  private MimeTypes(int id, String name, String fileExtension) {
    this.id = id;
    this.name = name;
    this.fileExtension = fileExtension;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getFileExtension() {
    return fileExtension;
  }

  public static MimeTypes getMimeType(int id) throws MimetypeException {
    for (MimeTypes mime : MimeTypes.values()) {
      if (mime.id == id) {
        return mime;
      }
    }
    throw new MimetypeException("The type mime with id " + id
        + " doesn't exist.");
  }

  public static MimeTypes getMimeType(String fileExtension)
      throws MimetypeException {
    for (MimeTypes mime : MimeTypes.values()) {
      if (mime.fileExtension.equals(fileExtension)) {
        return mime;
      }
    }
    throw new MimetypeException("The type mime with name " + fileExtension
        + " doesn't exist.");
  }
}
