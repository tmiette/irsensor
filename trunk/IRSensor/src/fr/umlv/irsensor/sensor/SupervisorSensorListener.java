package fr.umlv.irsensor.sensor;

public interface SupervisorSensorListener {
	
	public void confReceived(CatchArea area, int clock, int autonomy, int quality, int payload, int id);	
}
