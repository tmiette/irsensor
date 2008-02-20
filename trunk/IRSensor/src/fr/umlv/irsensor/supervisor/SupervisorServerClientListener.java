package fr.umlv.irsensor.supervisor;

import fr.umlv.irsensor.common.SensorConfiguration;

public interface SupervisorServerClientListener {

  public void ackConfPacketReceived(SensorNode node, SensorConfiguration conf);

}
