package fr.umlv.irsensor.supervisor.listeners;

import java.net.InetAddress;

import fr.umlv.irsensor.supervisor.SensorNode;

public interface SupervisorListener {

  public void sensorNodeConnected(SensorNode sensor, InetAddress inetAddress);

  public void sensorNodeConfigured(SensorNode sensor);

  public void sensorNodeStateChanged(SensorNode sensor);
  
  public void answerDataReceived(byte[] data);

}
