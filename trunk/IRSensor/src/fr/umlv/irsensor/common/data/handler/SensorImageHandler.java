package fr.umlv.irsensor.common.data.handler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;

import javax.imageio.ImageIO;

public class SensorImageHandler implements SensorHandler {

  @Override
  public BufferedImage byteArrayToData(byte[] bytes) {
    try {
      return ImageIO.read(new ByteArrayInputStream(bytes));
    } catch (IOException e) {
      return null;
    }
  }

  @Override
  public byte[] dataToByteArray(Object data, String name) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      BufferedImage imData = (BufferedImage) data;
      ImageIO.write(imData, URLConnection.guessContentTypeFromName(name), bos);
    } catch (IOException e) {
      System.err.println("Cannot get bytes from data");
      return new byte[0];
    }
    return bos.toByteArray();
  }

  @Override
  public BufferedImage mergeData(Object... datas) {
    if (datas.length == 0) {
      return null;
    }
    BufferedImage im = new BufferedImage(((BufferedImage) datas[0]).getWidth(),
        ((BufferedImage) datas[0]).getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    for (int i = 0; i < datas.length; i++) {
      BufferedImage image = (BufferedImage) datas[i];
      for (int j = 0; j < image.getHeight(null); j++) {
        for (int k = 0; k < image.getWidth(null); k++) {
          int rgb = image.getRGB(k, j);
          if (rgb != 0)
            im.setRGB(k, j, rgb);
        }
      }
    }
    return im;
  }

}
