package fr.umlv.irsensor.sensor;

import java.net.InetAddress;

import fr.umlv.irsensor.common.fields.CatchArea;

public interface SensorServerListener {

	public void helloRequestReceived(int id, InetAddress address);
	
	public void reqDataReceived(CatchArea cArea, int clock, int quality);
	
	public void repDataReceived(byte[] data, int mimetype);
}
