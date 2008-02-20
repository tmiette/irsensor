package fr.umlv.irsensor.dataserver;


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
	
	public ImageArea getImageArea(CatchArea area){
		CatchArea.Point origin = area.getP1();
		CatchArea.Point dst = area.getP2();
		int viewWidth = dst.getX() - origin.getX();
		int viewHeight = dst.getY() - origin.getY();
		BufferedImage im = image.getSubimage(origin.getX(), origin.getY(), viewWidth, viewHeight);
		return new ImageArea(im, area);
	}
	
	
	public BufferedImage createImageFromImageArea(ImageArea... images){
		if (images.length == 0) throw new IllegalArgumentException("No images given");
		BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
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



