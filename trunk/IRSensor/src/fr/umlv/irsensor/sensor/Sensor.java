package fr.umlv.irsensor.sensor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.SensorState;
import fr.umlv.irsensor.sensor.dispatcher.PacketDispatcher;
import fr.umlv.irsensor.sensor.dispatcher.exception.IdAlreadyUsedException;
import fr.umlv.irsensor.sensor.networkClients.DataClient;
import fr.umlv.irsensor.sensor.networkClients.SensorClient;
import fr.umlv.irsensor.sensor.networkClients.SupervisorClient;
import fr.umlv.irsensor.sensor.networkServer.SensorServer;
import fr.umlv.irsensor.sensor.networkServer.SupervisorSensorServer;
import fr.umlv.irsensor.util.Pair;

public class Sensor {

  private SensorConfiguration conf;

  private int id;

  private SensorState state = SensorState.DOWN;

  private final SupervisorClient supervisorClient;
  private DataClient dataClient;
  private SensorClient sensorClient;
  private SupervisorSensorServer supervisorServer;
  private SensorServer sensorServer;

  private final String supervisorServerAddr;
  private final String dataServerAddr;

  private final ArrayList<Pair<Integer, InetAddress>> children;

  private final ExecutorService executor = Executors.newFixedThreadPool(2);
  
  private int clockRequired = -1;

  private final ArrayList<Pair<byte[], Integer>> dataReceived;

  private final LinkedList<Pair<Date, byte[]>> capturedData;
  private static final int MAX_DATA_STORABLE = 10;

  public Sensor(final PacketDispatcher supervisorServer,
      final PacketDispatcher sensorServer, String dataServerAddr,
      String supervisorServerAddr) throws IOException {
    this.dataServerAddr = dataServerAddr;
    this.supervisorServerAddr = supervisorServerAddr;
    this.supervisorClient = new SupervisorClient(this);

    this.capturedData = new LinkedList<Pair<Date, byte[]>>();

    try {
      this.supervisorClient.registrySensor();
      System.out.println("id recu " + this.id);
      this.supervisorServer = new SupervisorSensorServer(this.id);
      this.supervisorServer
          .addSupervisorSensorListener(new SupervisorSensorListener() {
            @Override
            public void reqDataReceived(final CatchArea area, final int clock,
                final int quality) {
              if (children.size() > 0) {
                sendReqData(area, clock, quality);
              } else {
                sendRepData(clock);
              }
            }

            @Override
            public void confReceived(SensorConfiguration conf) {
              if (Sensor.this.conf == null || state.equals(SensorState.PAUSE)
                  || state.equals(SensorState.UP)) {
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

    this.children = new ArrayList<Pair<Integer, InetAddress>>();
    this.dataReceived = new ArrayList<Pair<byte[], Integer>>();
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

  private void startSensorServer(PacketDispatcher dispatcher, final int id) {
    this.sensorServer = new SensorServer(id);
    this.sensorServer.addSensorServerListener(new SensorServerListener() {
      @Override
      public void helloRequestReceived(int id, InetAddress address) {
        System.out.println("Hello request receveid to " + Sensor.this.id
            + " from " + id + ". Add it to my children list.");
        children.add(new Pair<Integer, InetAddress>(id, address));
      }

      @Override
      public void reqDataReceived(CatchArea area, int clock, int quality) {
        System.out.println(Sensor.this.id + " receive a req data");
        clockRequired = clock;
        if (children.size() > 0) {
          sendReqData(area, clock, quality);
        } else {
          /* Leaf code */
          sendRepData(clock);
        }
      }

      @Override
      public void repDataReceived(byte[] data, int mimeType) {
        dataReceived.add(new Pair<byte[], Integer>(data, mimeType));
        if (dataReceived.size() == children.size()) {
          // TODO Get data stored
          // TODO Get my dataClient
          // TODO Join all data
          // TODO Send it
          // sendRepData(); TODO clock missed
        }
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
        // do nothing, my father is correctly connected
        System.out.println("Hello reply receveid to " + Sensor.this.id);
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
    this.dataClient = new DataClient(this.id, this.conf.getCArea(), this.conf
        .getQuality(), this.conf.getClock(), this.dataServerAddr);
    this.dataClient.addSensorDataListener(new SensorDataListener() {
      @Override
      public void dataReceived(Date date, byte[] data) {
        /* Check if place is left in fifo */
        if (capturedData.size() >= MAX_DATA_STORABLE) {
          capturedData.removeLast();
        }
        /* Add captured data to fifo */
        capturedData.addFirst(new Pair<Date, byte[]>(date, data));
      }
    });
  }

  private void stopDataClient() {
    // TODO
  }

  private void sendReqData(final CatchArea cArea, final int clock,
      final int quality) {
    if (conf != null && state.equals(SensorState.UP)) {
      for (final Pair<Integer, InetAddress> pair : Sensor.this.children) {
        Sensor.this.executor.submit(new Runnable() {
          @Override
          public void run() {
            Sensor.this.sensorClient.sendReqData(pair.getSecondElement(), pair
                .getFirstElement(), cArea, clock, quality);
          };
        });
      }
    }
  }

  private void sendRepData(int date) {
    clockRequired = -1;
    System.out.println("Send rep data " + this.id);
    if (this.conf.getParentId() == -1) {
      /* Sink code */
      // this.supervisorClient.sendRepData(this.conf.getParentAddress(),
      // this.id);
      System.out.println("reponse finale");
    } else {
      byte[] data = null;
      for (Pair<Date, byte[]> CapData : this.capturedData) {
        // FIXME
        if (CapData.getFirstElement().getMinutes() == date) {
          data = CapData.getSecondElement();
        }
      }
      // TODO why send len in a repdata packet ?
      // TODO mimetype
      this.sensorClient.sendRepData(this.conf.getParentAddress(), this.conf
          .getParentId(), 0, data.length, data);
    }
  }

}
