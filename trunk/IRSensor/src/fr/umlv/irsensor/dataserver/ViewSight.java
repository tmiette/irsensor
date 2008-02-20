package fr.umlv.irsensor.dataserver;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.umlv.irsensor.sensor.CatchArea;


public class ViewSight {
	
	private BufferedImage image;
	
	public ViewSight(String file) {
		try {
			this.image = ImageIO.read(new File(file));	
		}
		catch (IOException e){
			System.err.println("Cannot read image "+file);
			System.exit(1);
		}
	}
	
	/**
	 * Retrieves a sub part of an image. This sub part corresponds the the area given as first parameter.
	 * @param area the area of the image to copy
	 * @return the sub image
	 */
	public ImageArea getImageArea(CatchArea area){
		if (area.getP1().getX() + area.getAreaWidth() > image.getWidth() ||
			area.getP1().getY() + area.getAreaHeight() > image.getHeight()
		) throw new IllegalArgumentException("Cannot retrieve a sub part of an image if it is larger that the main image file");
		CatchArea.Point origin = area.getP1();
		CatchArea.Point dst = area.getP2();
		int viewWidth = dst.getX() - origin.getX();
		int viewHeight = dst.getY() - origin.getY();
		BufferedImage im = image.getSubimage(origin.getX(), origin.getY(), viewWidth, viewHeight);
		return new ImageArea(im, area);
	}
	
	
	/**
	 * Creates an image using different areas
	 * @param images a list of images associated to the area representing a part of the main image
	 * @return the image reconstituted
	 */
	public BufferedImage createImageFromImageArea(ImageArea... images){
		if (images.length == 0) throw new IllegalArgumentException("No images given");
		BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = im.createGraphics();
		
		for (int i = 0; i < images.length; i++) {
			Image image = images[i].getImage();
			CatchArea imageArea = images[i].getArea();
			g.drawImage(image, imageArea.getP1().getX(), imageArea.getP1().getY(), imageArea.getAreaWidth(), imageArea.getAreaHeight(), null);
		}
		g.dispose();
		return im;
	}
	
}



