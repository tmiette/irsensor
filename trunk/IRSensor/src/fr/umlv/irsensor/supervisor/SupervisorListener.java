package fr.umlv.irsensor.supervisor;

import java.net.InetAddress;

public interface SupervisorListener {

  public void sensorNodeConnected(SensorNode sensor, InetAddress inetAddress);

}
