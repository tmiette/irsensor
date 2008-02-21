package fr.umlv.irsensor.sensor;

import java.io.IOException;

import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.sensor.dispatcher.PacketDispatcher;
import fr.umlv.irsensor.sensor.dispatcher.exception.IdAlreadyUsedException;
 
/**
 * Main class for the sensor application -- sensor.jar
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class Main {
	public static void main(String[] args) throws IOException, MalformedPacketException {
		
		if(args.length < 2){
			
		}
		
		//server part
		final PacketDispatcher supervisorDispatcher = new PacketDispatcher(IRSensorConfiguration.SERVER_PORT_LOCAL);
		
		final PacketDispatcher sensorDispatcher = new PacketDispatcher(31001);
		
		for(int i=0; i<7; i++){
			final Sensor sensor = new Sensor(supervisorDispatcher, sensorDispatcher);
		}
		
		supervisorDispatcher.startDispatcher();
		sensorDispatcher.startDispatcher();
	}
}