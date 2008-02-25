package fr.umlv.irsensor.common.data.handler;

import java.util.HashMap;

import fr.umlv.irsensor.common.data.MimeTypes;
import fr.umlv.irsensor.common.data.MimetypeException;

public class SensorHandlers {

  private static final HashMap<MimeTypes, SensorHandler> handlers = new HashMap<MimeTypes, SensorHandler>();

  static {
    SensorImageHandler image = new SensorImageHandler();
    handlers.put(MimeTypes.IMAGE_BMP, image);
    handlers.put(MimeTypes.IMAGE_GIF, image);
    //handlers.put(MimeTypes.IMAGE_JPEG, image);
    handlers.put(MimeTypes.IMAGE_JPG, image);
    handlers.put(MimeTypes.IMAGE_PNG, image);
    handlers.put(MimeTypes.IMAGE_TIFF, image);
  }

  private static SensorHandler ensureHandlerExists(MimeTypes mimeType)
      throws MimetypeException {
    SensorHandler handler = handlers.get(mimeType);
    if (handler == null)
      throw new MimetypeException("No mimetype correspondind to data "
          + mimeType);
    return handler;
  }

  public static byte[] dataToByteArray(Object data, MimeTypes mimeType)
      throws MimetypeException {
    SensorHandler handler = ensureHandlerExists(mimeType);
    return handler.dataToByteArray(data, mimeType.getFileExtension());
  }

  public static Object byteArrayToData(byte[] bytes, MimeTypes mimeType)
      throws MimetypeException {
    SensorHandler handler = ensureHandlerExists(mimeType);
    return handler.byteArrayToData(bytes);
  }

  public static Object mergeData(MimeTypes mimeType, Object... datas)
      throws MimetypeException {
    SensorHandler handler = ensureHandlerExists(mimeType);
    return handler.mergeData(datas);
  }
}
