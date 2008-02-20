package fr.umlv.irsensor.sensor;

import fr.umlv.irsensor.common.CatchArea;

public interface SupervisorSensorListener {
	
	public void confReceived(CatchArea area, int clock, int autonomy, int quality, int payload, int id);
	
	public void reqDataReceived(CatchArea area, int clock, int quality);
}
