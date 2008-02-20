package fr.umlv.irsensor.sensor;

import java.io.IOException;

import fr.umlv.irsensor.common.SensorState;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.sensor.networkClients.DataClient;
import fr.umlv.irsensor.sensor.networkClients.SensorClient;
import fr.umlv.irsensor.sensor.networkClients.SupervisorClient;

public class Sensor {

	private CatchArea area;
	private int quality;
	private int clock;
	private int autonomy;
	private int payload;
	
	private SensorState state = SensorState.DOWN;
	
	private final SupervisorClient supervisorClient;
	private final DataClient dataClient = new DataClient();
	private final SensorClient sensorClient = new SensorClient();
	
	public Sensor() throws IOException {
		this.supervisorClient = new SupervisorClient(this);
		try {
			this.supervisorClient.registrySensor();
			this.state = SensorState.UP;
		} catch (MalformedPacketException e) {
			e.printStackTrace();
		}
	}

	public CatchArea getArea() {
		return this.area;
	}

	public void setArea(CatchArea area) {
		this.area = area;
	}

	public int getQuality() {
		return this.quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getClock() {
		return this.clock;
	}

	public void setClock(int clock) {
		this.clock = clock;
	}

	public int getAutonomy() {
		return this.autonomy;
	}

	public void setAutonomy(int autonomy) {
		this.autonomy = autonomy;
	}

	public int getPayload() {
		return this.payload;
	}

	public void setPayload(int payload) {
		this.payload = payload;
	}

	public int getId() {
		return this.supervisorClient.getId();
	}
	
	public SupervisorClient getSupervisorClient(){
		return this.supervisorClient;
	}

}
