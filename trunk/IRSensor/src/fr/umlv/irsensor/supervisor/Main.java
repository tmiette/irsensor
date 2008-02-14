package fr.umlv.irsensor.supervisor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Main class for the supervisor server application -- supervisor.jar
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class Main {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		//start the supervisor server logger
		
		//load configuration file
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File("./conf/supervisor.conf")));
		final Supervisor supervisor = new Supervisor(Integer.valueOf((String)properties.get("NBR_SENSOR")), new SupervisorClient(),
		new SupervisorServer());
		
	}
}
