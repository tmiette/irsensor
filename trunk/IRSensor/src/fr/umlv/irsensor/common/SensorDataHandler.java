package fr.umlv.irsensor.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;

import javax.imageio.ImageIO;

public class SensorDataHandler {

	public static byte[] dataToByteArray(Object data, MimeTypes mimeType, String name){
		if (mimeType == MimeTypes.IMAGE){
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				BufferedImage imData = (BufferedImage)data;
				ImageIO.write(imData, URLConnection.guessContentTypeFromName(name), bos);
			}
			catch(IOException e){
				System.err.println("Cannot get bytes from data");
				return new byte[0];
			}
			return bos.toByteArray();
		}
		else {
			return null;
		}
	}

	public static Object byteArrayToData(byte[] bytes, MimeTypes mimeType){
		if (mimeType == MimeTypes.IMAGE){
			try { 
				return ImageIO.read(new ByteArrayInputStream(bytes));
			}
			catch (IOException e){
				return null;
			}
		}
		else {
			return null;
		}
	}

	public static Object mergeData(MimeTypes mimeType, Object... datas){
		if (mimeType == MimeTypes.IMAGE){
			if (datas.length == 0) throw new IllegalArgumentException("No image given");
		    BufferedImage im = new BufferedImage(((BufferedImage)datas[0]).getWidth(), ((BufferedImage)datas[0]).getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		    for (int i = 0; i < datas.length; i++) {
		      BufferedImage image = (BufferedImage)datas[i];
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
		return null;
	}
}
