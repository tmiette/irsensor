package fr.umlv.irsensor.supervisor.listeners;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.fields.SensorState;
import fr.umlv.irsensor.supervisor.SensorNode;

public interface SupervisorServerClientListener {

  public void sensorConfigurationChanged(SensorNode node, SensorConfiguration conf);

  public void sensorStateChanged(SensorNode node, SensorState state);
  
  public void answerDataReceived(byte[] data);
  
}
