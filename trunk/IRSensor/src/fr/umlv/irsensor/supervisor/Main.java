package fr.umlv.irsensor.supervisor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import fr.umlv.irsensor.supervisor.exception.ParsingConfigurationException;

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
		
		ConfigurationBuilder builder = null;
		try {
			builder = ConfigurationBuilder.load(new File("./conf/supervisor.conf"));
		} catch (ParsingConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final Supervisor supervisor = new Supervisor(builder.getSensorsNode(), new SupervisorServerClient(),
		new SupervisorServer());
		
	}
}
