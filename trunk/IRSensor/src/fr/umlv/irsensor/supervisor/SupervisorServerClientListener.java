package fr.umlv.irsensor.supervisor;

public interface SupervisorServerClientListener {

  public void ackConfPacketReceived(SensorNode sensor);

}
