package fr.umlv.irsensor.supervisor;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.SensorState;

public interface SupervisorServerClientListener {

  public void sensorConfigurationChanged(SensorNode node, SensorConfiguration conf);

  public void sensorStateChanged(SensorNode node, SensorState state);
  
  public void answerDataReceived(byte[] data);
  
}
