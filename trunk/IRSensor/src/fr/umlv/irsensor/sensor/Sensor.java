package fr.umlv.irsensor.sensor;

import java.io.IOException;
import java.net.InetAddress;

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
  private DataClient dataClient;
  private SensorClient sensorClient;
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
              if (conf != null && state.equals(SensorState.UP)) {

              }
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
              if (Sensor.this.conf != null) {
                Sensor.this.state = state;
                switch (state) {
                case DOWN:
                case PAUSE:
                  stopSensorServer();
                  stopSensorClient();
                  stopDataClient();
                  break;
                case UP:
                  startSensorServer(sensorServer, id);
                  startSensorClient(conf.getParentId(), id, conf
                      .getParentAddress());
                  startDataClient();
                  break;
                default:
                  break;
                }
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

  private void startSensorServer(PacketDispatcher dispatcher, int id) {
    this.sensorServer = new SensorServer(id);
    this.sensorServer.addSensorServerListener(new SensorServerListener() {
      @Override
      public void helloRequestReceived(int id, InetAddress address) {
        // TODO Auto-generated method stub

      }

      @Override
      public void reqDataReceived(CatchArea area, int clock, int quality) {
        // TODO Auto-generated method stub

      }
    });
    try {
      dispatcher.register(Sensor.this.sensorServer);
    } catch (IdAlreadyUsedException e) {
      e.printStackTrace();
    }
  }

  private void startSensorClient(int idD, int idS, InetAddress address) {
    this.sensorClient = new SensorClient();
    this.sensorClient.addSensorClientListener(new SensorClientListener() {
      @Override
      public void helloReplyReceived() {
        // TODO Auto-generated method stub

      }

      @Override
      public void repDataReceived(byte[] data) {
        // TODO Auto-generated method stub

      }
    });
    this.sensorClient.sendHelloRequest(idD, idS, address);
  }

  private void stopSensorServer() {
    // TODO
  }

  private void stopSensorClient() {
    // TODO
  }

  private void startDataClient() {
    this.dataClient = new DataClient();
    this.dataClient.addSensorDataListener(new SensorDataListener() {
      @Override
      public void dataReceived(byte[] data) {
        // TODO Auto-generated method stub

      }
    });
  }

  private void stopDataClient() {
    // TODO
  }

}
