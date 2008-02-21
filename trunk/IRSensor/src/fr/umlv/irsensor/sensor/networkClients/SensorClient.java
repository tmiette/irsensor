package fr.umlv.irsensor.sensor.networkClients;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import fr.umlv.irsensor.sensor.SensorClientListener;

public class SensorClient {
	
	private final List<SensorClientListener> listeners = new ArrayList<SensorClientListener>();
	
	public SensorClient(int id, InetAddress ipAddress) {
		sendHelloRequest(id, ipAddress);
	}
	
	public void sendHelloRequest(int id, InetAddress ipAddress){
		
	}
	
	public void addSensorClientListener(SensorClientListener listener){
		this.listeners.add(listener);
	}
	
	protected void fireRepDataReceived(){
		for(SensorClientListener l : this.listeners){
			l.helloReplyReceived();
		}
	}
	
	protected void fireHelloReplyReceived(byte[] data){
		for(SensorClientListener l : this.listeners){
			l.repDataReceived(data);
		}
	}
	
}
