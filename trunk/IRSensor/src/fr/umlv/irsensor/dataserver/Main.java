
package fr.umlv.irsensor.dataserver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import fr.umlv.irsensor.sensor.CatchArea;


public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setSize(600,600);
		ViewSight imageUtil = new ViewSight("src/images/code_sm.png");
		ImageArea top = imageUtil.getImageArea(new CatchArea(0,0,153,40));
		ImageArea bottom = imageUtil.getImageArea(new CatchArea(0,40,153,55));
		BufferedImage reconstitutedImage =  imageUtil.createImageFromImageArea(top, bottom);
		frame.getContentPane().add(new JLabel(new ImageIcon(reconstitutedImage)));
		frame.setVisible(true);
	}
}
