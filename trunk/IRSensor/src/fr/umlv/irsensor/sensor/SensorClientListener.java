package fr.umlv.irsensor.sensor;

public interface SensorClientListener {
	
	public void repDataReceived(byte[] data);
	
	public void helloReplyReceived();
	
}
