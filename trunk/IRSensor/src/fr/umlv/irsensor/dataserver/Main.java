
package fr.umlv.irsensor.dataserver;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import fr.umlv.irsensor.common.CatchArea;



public class Main {
	public static void main(String[] args) {
		
//		DataServerServer server = new DataServerServer(31002);
//		try  {
//			server.listen();
//		}
//		finally {
//			server.close();
//		}
		
		ViewSight completeCapturedZone = new ViewSight("./src/images/code_sm.png");
		final CatchArea c1 = new CatchArea(0,0,153,25);
		final CatchArea c2 = new CatchArea(0,25,153,55);
		final CatchArea c3 = new CatchArea(10,10,20,20);
		ImageArea imageArea1 = completeCapturedZone.getImageArea(c1);
		ImageArea imageArea2 = completeCapturedZone.getImageArea(c2);
		ImageArea imageArea3 = completeCapturedZone.getImageArea(c3);
		BufferedImage im = completeCapturedZone.createImageFromSubParts(imageArea1.getImage(), imageArea3.getImage());
		BufferedImage im2= completeCapturedZone.createImageFromSubParts(im, imageArea2.getImage());
	
		displayImage(im2);
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
