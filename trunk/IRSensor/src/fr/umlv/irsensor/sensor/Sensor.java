package fr.umlv.irsensor.sensor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.data.MimeTypes;
import fr.umlv.irsensor.common.data.MimetypeException;
import fr.umlv.irsensor.common.data.handler.SensorHandlers;
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

  private final String dataServerAddr;

  private final ArrayList<Pair<Integer, InetAddress>> children;

  private final ExecutorService executor = Executors.newFixedThreadPool(2);

  private int clockRequired = -1;

  private final ArrayList<Pair<byte[], Integer>> dataReceived;

  private final LinkedList<Pair<byte[], Long>> capturedData;
  private static final int MAX_DATA_STORABLE = 10;

  private MimeTypes mimeType;
  
  private final Object lock = new Object();

  public Sensor(final PacketDispatcher supervisorServer,
      final PacketDispatcher sensorServer, String dataServerAddr,
      String supervisorServerAddr) throws IOException {
    this.dataServerAddr = dataServerAddr;
    this.supervisorClient = new SupervisorClient(this);

    try {
      this.supervisorClient.registrySensor();
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
        // System.out.println("Hello request receveid to " +
        // Sensor.this.id
        // + " from " + id + ". Add it to my children list.");
        children.add(new Pair<Integer, InetAddress>(id, address));
      }

      @Override
      public void reqDataReceived(CatchArea area, int clock, int quality) {
        // System.out.println(Sensor.this.id + " receive a req data");
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
        synchronized (lock) {
                  dataReceived.add(new Pair<byte[], Integer>(data, mimeType));

        if (dataReceived.size() == children.size()) {
          // Get data stored
          byte[] dt = null;
          long time = System.currentTimeMillis() - clockRequired;
          time = 0l;
          for (Pair<byte[], Long> CapData : capturedData) {
            if (CapData.getSecondElement() <= time) {
              dt = CapData.getFirstElement();
              break;
            }
          }

          ArrayList<Object> dataToMerge = new ArrayList<Object>();
          if (dt == null) {
            // Date not found, do nothing
            IRSensorLogger.postMessage(Level.WARNING, "time not found " + time);
          } else {
            Object myData = null;
            try {
              myData = SensorHandlers.byteArrayToData(dt, MimeTypes
                  .getMimeType(mimeType));
              JFrame f = new JFrame("id : "+id);
              f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
              f.setSize(800, 600);
              f.setContentPane(new JLabel(new ImageIcon(dt)));
              f.setVisible(true);
            } catch (MimetypeException e) {
              // do nothing
            }
            if (myData != null) {
              dataToMerge.add(myData);
            }
          }

          for (Pair<byte[], Integer> pair : dataReceived) {
            Object sonData = null;
            try {
              sonData = SensorHandlers.byteArrayToData(pair.getFirstElement(),
                  MimeTypes.getMimeType(mimeType));
              if (sonData != null) {
                dataToMerge.add(sonData);
              }
            } catch (MimetypeException e) {
              // do nothing
            }

          }

          Object[] array = new Object[dataToMerge.size()];
          int i = 0;
          for (Object im : dataToMerge) {
            array[i++] = im;
          }

          byte[] dataToSend = new byte[0];
          Object mergedImage = null;
          if (array.length > 0) {
            try {
              MimeTypes mime = MimeTypes.getMimeType(mimeType);
              mergedImage = SensorHandlers.mergeData(mime, (Object[]) array);
              if (mergedImage != null) {
                dataToSend = SensorHandlers.dataToByteArray(mergedImage, mime);
              }
            } catch (MimetypeException e) {
              // do noting
            }
          }
          
          if (conf.getParentId() == -1) {
            /* Sink code */
            supervisorClient.sendRepData(id, mimeType, dataToSend.length,
                dataToSend);
          } else {
            sensorClient.sendRepData(conf.getParentAddress(), conf
                .getParentId(), mimeType, dataToSend.length, dataToSend);
          }
        }
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
      public void dataReceived(long currentDate, int mimeType, byte[] data) {
        /* Check if place is left in fifo */
        if (capturedData.size() >= MAX_DATA_STORABLE) {
          capturedData.removeLast();
        }
        try {
          Sensor.this.mimeType = MimeTypes.getMimeType(mimeType);
        } catch (MimetypeException e) {
          // do nothing
        }
        /* Add captured data to fifo */
        currentDate = 0l;
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
    synchronized (lock) {
      clockRequired = -1;
      if (this.conf.getParentId() == -1) {
        /* Sink code */
        // FIXME
        System.out.println("JE NE DOIS JAMAIS PASSER LA !!!!!!!!!!!!!!!!!!!");
        // supervisorClient.sendRepData(id);
      } else {
        byte[] data = null;
        long time = System.currentTimeMillis() - date;
        time = 0l;
        // TODO

        for (Pair<byte[], Long> CapData : capturedData) {
          if (CapData.getSecondElement() <= time) {
            data = CapData.getFirstElement();
            break;
          }
        }

        if (data == null) {
          data = new byte[0];
        }
        
        JFrame f = new JFrame("id : "+id);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setSize(800, 600);
        f.setContentPane(new JLabel(new ImageIcon(data)));
        f.setVisible(true);

        this.sensorClient.sendRepData(this.conf.getParentAddress(), this.conf
            .getParentId(), this.mimeType.getId(), data.length, data);

        this.dataReceived.clear();
      }
    }
  }

}
