package fr.umlv.irsensor.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class defines a logger where application messages could be posted
 * 
 * <p>
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 * <p>
 * 
 * @version 1.0
 */
public class IRSensorLogger {
	
	private static final String loggerName = "irSensorLogger";
	
	private static final Logger logger = Logger.getLogger(loggerName);

	public static void startLogger(String filename) {
		try {
			final FileHandler file = new FileHandler("log/"+filename, true);
			logger.addHandler(file);
			logger.setLevel(Level.ALL);
			file.setFormatter(new SimpleFormatter());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void postMessage(Level level, String msg){
		logger.log(level, msg);
	}
	
	private IRSensorLogger() {
	}
}
