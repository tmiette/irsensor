package fr.umlv.irsensor.sensor;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.SensorState;

public interface SupervisorSensorListener {
	
	public void confReceived(SensorConfiguration conf);
	
	public void reqDataReceived(CatchArea area, int clock, int quality);
	
	public void stateChanged(SensorState state);
}
