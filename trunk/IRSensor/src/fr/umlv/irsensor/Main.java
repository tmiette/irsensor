package fr.umlv.irsensor;

import java.io.IOException;

import fr.umlv.irsensor.supervisor.BufferFactory;
import fr.umlv.irsensor.supervisor.SupervisorServer;


public class Main {

  public static void main(String[] args) throws IOException {
	  BufferFactory fact = new BufferFactory();
	  fact.createRepConPacket(5);
  }

}
