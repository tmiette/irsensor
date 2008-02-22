package fr.umlv.irsensor.sensor;

public interface SensorDataListener {
	
	public void dataReceived(long currentDate, byte[] data);
	
}
