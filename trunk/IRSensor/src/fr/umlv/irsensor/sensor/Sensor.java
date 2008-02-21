package fr.umlv.irsensor.sensor;

import java.io.IOException;

import fr.umlv.irsensor.common.CatchArea;
import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.SensorState;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.sensor.dispatcher.PacketDispatcher;
import fr.umlv.irsensor.sensor.dispatcher.exception.IdAlreadyUsedException;
import fr.umlv.irsensor.sensor.networkClients.DataClient;
import fr.umlv.irsensor.sensor.networkClients.SensorClient;
import fr.umlv.irsensor.sensor.networkClients.SupervisorClient;
import fr.umlv.irsensor.sensor.networkServer.SensorServer;
import fr.umlv.irsensor.sensor.networkServer.SupervisorSensorServer;

public class Sensor {

  private SensorConfiguration conf;

  private int id;

  private SensorState state = SensorState.DOWN;

  private final SupervisorClient supervisorClient;
  private final DataClient dataClient = new DataClient();
  private SensorClient sensorClient = new SensorClient();

  private SupervisorSensorServer supervisorServer;
  private SensorServer sensorServer;

  public Sensor(final PacketDispatcher supervisorServer,
      final PacketDispatcher sensorServer) throws IOException {
    this.supervisorClient = new SupervisorClient(this);

    try {
      this.supervisorClient.registrySensor();
      System.out.println("id recu " + this.id);
      this.supervisorServer = new SupervisorSensorServer(this.id);
      this.supervisorServer
          .addSupervisorSensorListener(new SupervisorSensorListener() {
            @Override
            public void reqDataReceived(CatchArea area, int clock, int quality) {

            }

            @Override
            public void confReceived(SensorConfiguration conf) {
              if (conf == null || state.equals(SensorState.PAUSE)
                  || state.equals(SensorState.PAUSE)) {
                Sensor.this.conf = conf;
                System.out.println("New conf received.");
              }
            }

            @Override
            public void stateChanged(SensorState state) {
              Sensor.this.state = state;
              switch (state) {
              case DOWN:
                // TODO shutdown sensorClient et sensorServer
                break;
              case PAUSE:
                // TODO shutdown sensorClient et sensorServer
                break;
              case UP:
                Sensor.this.sensorServer = new SensorServer(id);
                Sensor.this.sensorClient = new SensorClient();
                try {
                  sensorServer.register(Sensor.this.sensorServer);
                } catch (IdAlreadyUsedException e) {
                  e.printStackTrace();
                }
                break;
              default:
                break;
              }
            }
          });

      supervisorServer.register(this.supervisorServer);
    } catch (MalformedPacketException e) {
      e.printStackTrace();
    } catch (IdAlreadyUsedException e) {
      e.printStackTrace();
    }
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  public SensorConfiguration getConfiguration() {
    return this.conf;
  }

  public SupervisorClient getSupervisorClient() {
    return this.supervisorClient;
  }
}
