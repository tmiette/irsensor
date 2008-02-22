
package fr.umlv.irsensor.dataserver;

import java.util.logging.Level;

import fr.umlv.irsensor.util.IRSensorLogger;

public class Main {
	public static void main(String[] args) {
		IRSensorLogger.startLogger("data");
		IRSensorLogger.postMessage(Level.FINE, "Data Server Application is started");
		
		DataServerServer server = new DataServerServer();
		try  {
			server.listen();
		}
		finally {
			server.close();
		}
	}
}
//		
//		ViewSight completeCapturedZone = new ViewSight("./src/images/code_sm.png");
//		final CatchArea c1 = new CatchArea(10,0,100,25);
//		final CatchArea c3 = new CatchArea(10,35,50,40);
//		BufferedImage imageArea1 = completeCapturedZone.getSubImage(c1);
//		BufferedImage imageArea2 = completeCapturedZone.getSubImage(c3);
//		BufferedImage im = completeCapturedZone.createImageFromSubParts(imageArea1, imageArea2);
//		BufferedImage reduceIm = completeCapturedZone.reduceImage(im);
//		displayImage(reduceIm);
	
//	
//	private static void displayImage(BufferedImage zone){
//		JFrame frame = new JFrame();
//		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//		frame.setSize(600,600);
//		final ImageIcon imageIcon = new ImageIcon(zone);
//		frame.getContentPane().add(new JLabel(imageIcon));
//		frame.setVisible(true);
//	}
//}
