package fr.umlv.irsensor;

import java.io.IOException;
import java.util.logging.Level;

import fr.umlv.irsensor.sensor.Sensor;
import fr.umlv.irsensor.sensor.dispatcher.PacketDispatcher;
import fr.umlv.irsensor.sensor.dispatcher.exception.IdAlreadyUsedException;
import fr.umlv.irsensor.supervisor.SupervisorServer;
import fr.umlv.irsensor.util.IRSensorLogger;

public class Main {

  public static void main(String[] args) throws IOException {
	  
	  //Starts the logger and notifies the application's start
	  IRSensorLogger.startLogger();
	  IRSensorLogger.postMessage(Level.FINE, "Application is started");
	  
	  
	  final SupervisorServer server = new SupervisorServer();
	  //server.launch();
	  
	  final PacketDispatcher dispatcher = new PacketDispatcher(31000);
	  for(int i=0; i<2; i++){
		  final Sensor sensor = new Sensor();
		  try {
			dispatcher.register(sensor.getSupervisorClient());
		} catch (IdAlreadyUsedException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Sensor "+i+" is registered");
	  }
	  dispatcher.startDispatcher();
  }
}
