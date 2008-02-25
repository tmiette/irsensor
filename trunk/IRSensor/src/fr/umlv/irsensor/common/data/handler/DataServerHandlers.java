package fr.umlv.irsensor.common.data.handler;

import java.net.URLConnection;

import fr.umlv.irsensor.common.data.MimeTypes;
import fr.umlv.irsensor.common.data.MimetypeException;

public class DataServerHandlers {

  public static DataServerHandler getHandlerWithMimeType(String fileName) {

    String mimeString = URLConnection.guessContentTypeFromName(fileName);

    String[] split = mimeString.split("/");
    if (split.length < 2) {
      return null;
    }

    String fileExtension = split[1];

    // hack for jpeg filess
    if (fileExtension.equals("jpeg")) {
      fileExtension = "jpg";
    }

    MimeTypes mime = null;
    try {
      mime = MimeTypes.getMimeType(fileExtension);
    } catch (MimetypeException e) {
      return null;
    }

    switch (mime) {
    case IMAGE_BMP:
      return new ImageDataHandler(MimeTypes.IMAGE_BMP, fileName, fileExtension);
    case IMAGE_GIF:
      return new ImageDataHandler(MimeTypes.IMAGE_GIF, fileName, fileExtension);
    case IMAGE_JPG:
      return new ImageDataHandler(MimeTypes.IMAGE_JPG, fileName, fileExtension);
    case IMAGE_PNG:
      return new ImageDataHandler(MimeTypes.IMAGE_PNG, fileName, fileExtension);
    case IMAGE_TIFF:
      return new ImageDataHandler(MimeTypes.IMAGE_TIFF, fileName, fileExtension);
    case TEXT:
      return null;
    default:
      return null;
    }
  }

}
