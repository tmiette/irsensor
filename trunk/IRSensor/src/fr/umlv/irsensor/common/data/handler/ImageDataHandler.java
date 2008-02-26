package fr.umlv.irsensor.common.data.handler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.umlv.irsensor.common.data.MimeTypes;
import fr.umlv.irsensor.common.fields.CatchArea;

public class ImageDataHandler implements DataServerHandler {

  private BufferedImage image;

  private final String fileExtension;

  private final MimeTypes mime;

  public ImageDataHandler(MimeTypes mime, String imageName, String fileExtension) {
    this.mime = mime;
    this.fileExtension = fileExtension;
    try {
      final File resource = new File(imageName);
      image = ImageIO.read(resource);
    } catch (IOException e) {
      System.err.println("Cannot read image " + imageName);
      System.exit(1);
    }
  }

  @Override
  public MimeTypes getMimeType() {
    return this.mime;
  }

  @Override
  public BufferedImage getSubData(CatchArea area) {
    if (area.getP1().getX() + area.getAreaWidth() > image.getWidth()
        || area.getP1().getY() + area.getAreaHeight() > image.getHeight())
      throw new IllegalArgumentException(
          "Cannot retrieve a sub part of an image if it is larger that the main image file");
    BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(),
        BufferedImage.TYPE_4BYTE_ABGR);
    for (int j = 0; j < image.getHeight(null); j++) {
      for (int k = 0; k < image.getWidth(null); k++) {
        if (k >= area.getP1().getX() && k <= area.getP2().getX()
            && j >= area.getP1().getY() && j <= area.getP2().getY())
          im.setRGB(k, j, image.getRGB(k, j));
      }
    }
    return im;
  }

  @Override
  public byte[] dataToByteArray(Object data) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      BufferedImage imData = (BufferedImage) data;
      System.out.println("file ext " + fileExtension);
      ImageIO.write(imData, fileExtension, bos);
    } catch (IOException e) {
      System.err.println("Cannot get bytes from data");
      return new byte[0];
    }
    return bos.toByteArray();
  }
}
