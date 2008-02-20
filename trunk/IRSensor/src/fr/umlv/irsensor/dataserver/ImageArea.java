package fr.umlv.irsensor.dataserver;

import java.awt.image.BufferedImage;

import fr.umlv.irsensor.common.CatchArea;

public class ImageArea {
	
	private final BufferedImage image;
	private final CatchArea area;
	
	public ImageArea(BufferedImage image, CatchArea area) {
		this.image = image;
		this.area = area;
	}
	
	public CatchArea getArea() {
		return area;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
}
