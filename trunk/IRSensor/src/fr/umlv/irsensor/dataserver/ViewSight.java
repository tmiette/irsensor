package fr.umlv.irsensor.dataserver;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.umlv.irsensor.common.CatchArea;
import fr.umlv.irsensor.common.CatchArea.Point;


public class ViewSight {
	
	private BufferedImage image;
	private String fileExtension = null;
	
	public ViewSight(String file) {
		try {
			final File resource = new File(file);
			int dot = -1;
			if ((dot = file.lastIndexOf(".")) != -1){
				this.fileExtension = file.substring(dot + 1);
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
	public String getFileExtension(){
		if (fileExtension == null)
			throw new IllegalStateException("Could not retrieve file extension in filename");
		return fileExtension;
	}
	
	
	/**
	 * Retrieves a sub part of an image. 
	 * @param area the area of the image to copy
	 * @return the sub image
	 */
	public ImageArea getImageArea(CatchArea area){
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
		return new ImageArea(im, area);
	}
	
	
	/**
	 * Reconstitutes the main image from sub image parts extracted from the main image.
	 * @param images a list of buffered images representing a sub part of the main image
	 * @return the new reconstituted image
	 */
	public BufferedImage createImageFromSubParts(BufferedImage... images){
		if (images.length == 0) throw new IllegalArgumentException("No image given");
		BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
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



