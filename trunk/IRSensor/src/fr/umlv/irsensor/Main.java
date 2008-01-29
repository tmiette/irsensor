package fr.umlv.irsensor;

import java.io.IOException;

import fr.umlv.irsensor.sensor.SupervisorSensorClient;

public class Main {

  public static void main(String[] args) throws IOException {

    new SupervisorSensorClient().launch();

  }

}
