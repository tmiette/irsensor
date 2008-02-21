
package fr.umlv.irsensor.dataserver;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;



public class Main {
	public static void main(String[] args) {
		
		DataServerServer server = new DataServerServer(31002);
		try  {
			server.listen();
		}
		finally {
			server.close();
		}
		
		
//		ViewSight completeCapturedZone = new ViewSight("./src/images/code_sm.png");
//		final CatchArea c1 = new CatchArea(0,0,153,25);
//		final CatchArea c2 = new CatchArea(0,25,153,55);
//		final CatchArea c3 = new CatchArea(10,10,20,20);
//		BufferedImage imageArea1 = completeCapturedZone.getSubImage(c1);
//		BufferedImage imageArea2 = completeCapturedZone.getSubImage(c2);
//		BufferedImage imageArea3 = completeCapturedZone.getSubImage(c3);
//		BufferedImage im = completeCapturedZone.createImageFromSubParts(imageArea1, imageArea3);
//		BufferedImage im2= completeCapturedZone.createImageFromSubParts(im, imageArea2);
//		displayImage(im2);
	}
	
	private static void displayImage(BufferedImage zone){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setSize(600,600);
		final ImageIcon imageIcon = new ImageIcon(zone);
		frame.getContentPane().add(new JLabel(imageIcon));
		frame.setVisible(true);
	}
}
