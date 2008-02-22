package fr.umlv.irsensor.sensor;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import sun.rmi.server.Dispatcher;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.SensorState;
import fr.umlv.irsensor.dataserver.ViewSight;
import fr.umlv.irsensor.sensor.dispatcher.PacketDispatcher;
import fr.umlv.irsensor.sensor.dispatcher.exception.IdAlreadyUsedException;
import fr.umlv.irsensor.sensor.networkClients.DataClient;
import fr.umlv.irsensor.sensor.networkClients.SensorClient;
import fr.umlv.irsensor.sensor.networkClients.SupervisorClient;
import fr.umlv.irsensor.sensor.networkServer.SensorServer;
import fr.umlv.irsensor.sensor.networkServer.SupervisorSensorServer;
import fr.umlv.irsensor.util.IRSensorLogger;
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

  private final LinkedList<Pair<byte[], Long>> capturedData;
  private static final int MAX_DATA_STORABLE = 10;

  public Sensor(final PacketDispatcher supervisorServer,
      final PacketDispatcher sensorServer, String dataServerAddr,
      String supervisorServerAddr) throws IOException {
    this.dataServerAddr = dataServerAddr;
    this.supervisorServerAddr = supervisorServerAddr;
    this.supervisorClient = new SupervisorClient(this);

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
                  stopSensorServer(sensorServer);
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
    this.capturedData = new LinkedList<Pair<byte[], Long>>();
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
        IRSensorLogger.postMessage(Level.INFO, "repDataReceived");
        dataReceived.add(new Pair<byte[], Integer>(data, mimeType));

        if (dataReceived.size() == children.size()) {
          // Get data stored
          byte[] dt = null;
          long time = System.currentTimeMillis() - clockRequired;

          for (Pair<byte[], Long> CapData : capturedData) {
            if (CapData.getSecondElement() <= time) {
              IRSensorLogger.postMessage(Level.INFO, "time found " + time);
              dt = CapData.getFirstElement();
              break;
            }
          }

          ArrayList<BufferedImage> imagesToMerge = new ArrayList<BufferedImage>();
          if (dt == null) {
            // Date not found
            IRSensorLogger.postMessage(Level.WARNING, "time found " + time);
            // TODO
          } else {
            BufferedImage myImage = null;
            try {
              myImage = ImageIO.read(new ByteArrayInputStream(dt));
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            if (myImage != null) {
              imagesToMerge.add(myImage);
            }
          }

          for (Pair<byte[], Integer> pair : dataReceived) {
            BufferedImage sonImage = null;
            try {
              sonImage = ImageIO.read(new ByteArrayInputStream(pair
                  .getFirstElement()));
              imagesToMerge.add(sonImage);
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }

          }

          BufferedImage result = ViewSight
              .createImageFromSubParts((BufferedImage[]) imagesToMerge
                  .toArray());
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          try {
            ImageIO.write(result, ViewSight.getFileExtension(), bos);
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          sendRepData(clockRequired);
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

  private void stopSensorServer(PacketDispatcher dispatcher) {
    dispatcher.unregister(id);
  }

  private void stopSensorClient() {
    // TODO
  }

  private void startDataClient() {
    this.dataClient = new DataClient(this.id, this.conf.getCArea(), this.conf
        .getQuality(), this.conf.getClock(), this.dataServerAddr);
    this.dataClient.addSensorDataListener(new SensorDataListener() {
      @Override
      public void dataReceived(long currentDate, byte[] data) {
        /* Check if place is left in fifo */
        if (capturedData.size() >= MAX_DATA_STORABLE) {
          capturedData.removeLast();
        }
        /* Add captured data to fifo */
        capturedData.addFirst(new Pair<byte[], Long>(data, currentDate));
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
      long time = System.currentTimeMillis() - date;

      for (Pair<byte[], Long> CapData : capturedData) {
        if (CapData.getSecondElement() <= time) {
          data = CapData.getFirstElement();
          break;
        }
      }
      // TODO mimetype
      if (data != null) {
        this.sensorClient.sendRepData(this.conf.getParentAddress(), this.conf
            .getParentId(), 0, data.length, data);
      }
    }
  }

}
