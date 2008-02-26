package fr.umlv.irsensor.sensor;

import java.io.IOException;
import java.util.logging.Level;

import fr.umlv.irsensor.common.IRSensorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.sensor.dispatcher.PacketDispatcher;
import fr.umlv.irsensor.sensor.dispatcher.exception.IdAlreadyUsedException;
import fr.umlv.irsensor.util.IRSensorLogger;
 
/**
 * Main class for the sensor application -- sensor.jar
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class SensorMain {
	public static void main(String[] args) throws IOException, MalformedPacketException {
		
		if(args.length < 3){
			System.out.println("Main <sensors> <data server ip> <supervisor server ip>");
			System.exit(1);
		}
		
		IRSensorLogger.startLogger("sensor");
		IRSensorLogger.postMessage(Level.FINE, "Sensor Application is started");
		
		final String dataServerIpAddress = args[1];
		final String supervisorServerIpAddress = args[2];
		
		//server part
		final PacketDispatcher supervisorDispatcher = new PacketDispatcher(IRSensorConfiguration.SUPERVISOR_SERVER_PORT, "Supervisor Dispatcher");
		
		final PacketDispatcher sensorDispatcher = new PacketDispatcher(IRSensorConfiguration.SENSOR_SERVER_PORT, "Sensor Dispatcher");
		
		for(int i=0; i<Integer.parseInt(args[0]); i++){
			final Sensor sensor = new Sensor(supervisorDispatcher, sensorDispatcher, dataServerIpAddress, 
					supervisorServerIpAddress);
		}
		
		supervisorDispatcher.startDispatcher();
		sensorDispatcher.startDispatcher();
	}
}