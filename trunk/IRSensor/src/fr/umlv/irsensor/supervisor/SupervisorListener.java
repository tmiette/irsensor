package fr.umlv.irsensor.supervisor;

import java.net.InetAddress;

public interface SupervisorListener {

  public void sensorNodeConnected(SensorNode sensor, InetAddress inetAddress);

  public void sensorNodeConfigured(SensorNode sensor);

  public void sensorNodeStateChanged(SensorNode sensor);
  
  public void answerDataReceived(byte[] data);

}
