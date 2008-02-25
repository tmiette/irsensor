package fr.umlv.irsensor.supervisor;

import java.nio.ByteBuffer;
import java.util.logging.Level;

import javax.swing.SwingUtilities;

import fr.umlv.irsensor.gui.MainFrame;
import fr.umlv.irsensor.util.IRSensorLogger;

/**
 * Main class for the supervisor server application -- supervisor.jar
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class SupervisorMain {
	public static void main(String[] args) {

		IRSensorLogger.postMessage(Level.FINE, "Supervisor Application is started");
		ByteBuffer bb = ByteBuffer.allocate(10000);
		// schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainFrame().launch();
			}
		});
		try {
			Thread.sleep(10000);
			bb = null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
