package fr.umlv.irsensor.dataserver;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.umlv.irsensor.common.fields.CatchArea;


public class ViewSight {

	private BufferedImage image;
	private static String fileExtension = null;

	public ViewSight(String file) {
		try {
			final File resource = new File(file);
			int dot = -1;
			if ((dot = file.lastIndexOf(".")) != -1){
				fileExtension = file.substring(dot + 1);
			}
			this.image = ImageIO.read(resource);	
		}
		catch (IOException e){
			System.err.println("Cannot read image "+file);
			System.exit(1);
		}
	}

	/**
	 * Retrieves the file extension that describes the data hold by the data server
	 * @return the file extension
	 */
	public static String getFileExtension(){
		if (fileExtension == null)
			throw new IllegalStateException("Could not retrieve file extension in filename");
		return fileExtension;
	}


	/**
	 * Retrieves a sub part of an image. 
	 * @param area the area of the image to copy
	 * @return the sub image
	 */
	public BufferedImage getSubImage(CatchArea area){
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


	/**
	 * Reduces the size of an image discarding useless pixels
	 * @param image the image to reduce
	 * @return the new reduced image
	 */
	public BufferedImage reduceImage(BufferedImage image){
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


	/**
	 * Reconstitutes the main image from sub image parts extracted from the main image.
	 * @param images a list of buffered images representing a sub part of the main image
	 * @return the new reconstituted image
	 */
//	public BufferedImage createImageFromSubParts(BufferedImage... images){
//		if (images.length == 0) throw new IllegalArgumentException("No image given");
//		BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
//		for (int i = 0; i < images.length; i++) {
//			BufferedImage image = images[i];
//			for (int j = 0; j < image.getHeight(null); j++) {
//				for (int k = 0; k < image.getWidth(null); k++) {
//					int rgb = image.getRGB(k, j);
//					if (rgb != 0)
//						im.setRGB(k, j, rgb);
//				}
//			}
//		}
//		return im;
//	}

	
	 public static BufferedImage createImageFromSubParts(BufferedImage... images){
	    if (images.length == 0) throw new IllegalArgumentException("No image given");
	    BufferedImage im = new BufferedImage(images[0].getWidth(), images[0].getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
	    for (int i = 0; i < images.length; i++) {
	      BufferedImage image = images[i];
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



