package fr.umlv.irsensor.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import fr.umlv.irsensor.common.fields.CatchArea;

public class ImageDataHandler implements DataServerHandler  {

	private BufferedImage image;
	private String imageExtension;
	
	public ImageDataHandler(String imageName) {
		try {
			final File resource = new File(imageName);
			int dot = imageName.lastIndexOf(".");
			if (dot == -1) throw new IllegalArgumentException("Image without extension");
			imageExtension = imageName.substring(dot + 1);	
			image = ImageIO.read(resource);	
		}
		catch (IOException e){
			System.err.println("Cannot read image "+imageName);
			System.exit(1);
		}
	}

	@Override
	public BufferedImage getSubData(CatchArea area) {
		if (area.getP1().getX() + area.getAreaWidth() > image.getWidth() ||
				area.getP1().getY() + area.getAreaHeight() > image.getHeight()
		) throw new IllegalArgumentException("Cannot retrieve a sub part of an image if it is larger that the main image file");
		BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		for (int j = 0; j < image.getHeight(null); j++) {
			for (int k = 0; k < image.getWidth(null); k++) {
				if (k >= area.getP1().getX() && k <= area.getP2().getX() && j >= area.getP1().getY() && j <= area.getP2().getY())
					im.setRGB(k, j, image.getRGB(k, j));
			}
		}
		return im;
	}

	@Override
	public BufferedImage reduceData(Object data) {
		final int height = image.getHeight();
		final int width = image.getWidth();
		int minX1 = width, minY1 = height;
		int maxX2 = 0, maxY2 = 0;
		// Retrieve the effective drawn zone 
		for (int j = 0; j < height; j++) {
			for (int k = 0; k < width; k++) {
				int rgb = image.getRGB(k, j);
				if (rgb == 0) continue;
				minX1 = Math.min(minX1, k);
				minY1 = Math.min(minY1, j);
				maxX2 = Math.max(maxX2, k);
				maxY2 = Math.max(maxY2, j);

			}
		}
		// Create the reduced image
		int newWidth = maxX2 - minX1; int newHeight = maxY2 - minY1;
		System.out.println("New width : "+newWidth+" height "+newHeight);
		BufferedImage im = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_4BYTE_ABGR);
		for (int j = 0; j < newHeight; j++) {
			for (int k = 0; k < newWidth; k++) {
				int rgb = image.getRGB(k + minX1, j + minY1);
				im.setRGB(k, j, rgb);
			}
		}
		return im;
	}

	@Override
	public byte[] dataToByteArray(Object data, String name) {
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
}
