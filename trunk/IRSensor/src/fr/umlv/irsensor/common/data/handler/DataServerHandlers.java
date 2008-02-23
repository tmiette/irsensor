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

    MimeTypes mime = null;
    try {
      mime = MimeTypes.getMimeType(split[0]);
    } catch (MimetypeException e) {
      return null;
    }

    switch (mime) {
    case IMAGE:
      return new ImageDataHandler(fileName, fileExtension);
    case TEXT:
      return null;
    default:
      return null;
    }
  }

}
