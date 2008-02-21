package fr.umlv.irsensor.gui;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import fr.umlv.irsensor.common.CatchArea;
import fr.umlv.irsensor.supervisor.SensorNode;
import fr.umlv.irsensor.supervisor.Supervisor;
import fr.umlv.irsensor.supervisor.SupervisorListener;

public class RequestModel {
	
	public final Supervisor supervisor;
	
	public final List<RequestListener> listeners = new ArrayList<RequestListener>();
	
	public RequestModel(Supervisor supervisor) {
		this.supervisor = supervisor;
		this.supervisor.addSupervisorListener(new SupervisorListener() {
			@Override
			public void answerDataReceived(Object data) {
				fireAnswerReceived(data);
			}
			@Override
			public void sensorNodeConnected(SensorNode sensor, InetAddress inetAddress) {}

			@Override
			public void sensorNodeConfigured(SensorNode sensor) {}

			@Override
			public void sensorNodeStateChanged(SensorNode sensor) {}
		});
	}
	
	public void submitRequest(final CatchArea cArea, final int quality, final int clock){
		new Thread(new Runnable(){
			@Override
			public void run() {
				RequestModel.this.supervisor.dataRequest(cArea, quality, clock);
			}	
		}).start();
		
	}
	
	public void addRequestListener(RequestListener listener){
		this.listeners.add(listener);
	}
	
	protected void fireAnswerReceived(Object data){
		for(RequestListener l: this.listeners){
			l.answerReceived(data);
		}
	}
}
