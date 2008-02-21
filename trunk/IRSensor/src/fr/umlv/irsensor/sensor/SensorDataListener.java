package fr.umlv.irsensor.sensor;

import java.util.Date;

public interface SensorDataListener {
	
	public void dataReceived(Date date, byte[] data);
	
}
