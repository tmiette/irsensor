package fr.umlv.irsensor.gui;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.SensorState;
import fr.umlv.irsensor.supervisor.SensorNode;
import fr.umlv.irsensor.supervisor.Supervisor;
import fr.umlv.irsensor.supervisor.SupervisorListener;

public class SupervisorTableModel extends AbstractTableModel {

  private static final long serialVersionUID = -6940960745068220341L;

  private final ExecutorService executor = Executors.newFixedThreadPool(5);

  private final Supervisor supervisor;

  public SupervisorTableModel(Supervisor supervisor) {
    this.supervisor = supervisor;
    this.supervisor.addSupervisorListener(new SupervisorListener() {
      @Override
      public void sensorNodeConnected(SensorNode sensor, InetAddress inetAddress) {
        fireTableRowsInserted(getRowCount(), getRowCount());
      }

      @Override
      public void sensorNodeConfigured(SensorNode sensor) {
        for (int i = 0; i < getRowCount(); i++) {
          fireTableCellUpdated(i, 1);
          fireTableCellUpdated(i, 5);
          fireTableCellUpdated(i, 6);
          fireTableCellUpdated(i, 7);
          fireTableCellUpdated(i, 8);
          fireTableCellUpdated(i, 9);
          fireTableCellUpdated(i, 10);
        }
      }

      @Override
      public void sensorNodeStateChanged(SensorNode sensor) {
        System.out.println("State Changed " + sensor.getState());
        for (int i = 0; i < getRowCount(); i++) {
          fireTableCellUpdated(i, 4);
        }
      }

      @Override
      public void answerDataReceived(byte[] data) {
      }
    });
  }

  @Override
  public int getColumnCount() {
    return 11;
  }

  @Override
  public int getRowCount() {
    return this.supervisor.getSensorNodes().size();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    SensorNode sensor = this.supervisor.getSensorNodes().get(rowIndex);
    switch (columnIndex) {
    case 0:
      return sensor.isConnected();
    case 1:
      return sensor.isConfigured();
    case 2:
      return sensor.getId();
    case 3:
      return sensor.getAddress();
    case 4:
      return sensor.getState();
    case 5:
      return sensor.getCArea();
    case 6:
      return sensor.getAutonomy();
    case 7:
      return sensor.getClock();
    case 8:
      return sensor.getPayload();
    case 9:
      return sensor.getQuality();
    case 10:
      return sensor.getParentId();
    default:
      return null;
    }
  }

  @Override
  public String getColumnName(int column) {
    switch (column) {
    case 0:
      return "connected";
    case 1:
      return "configured";
    case 2:
      return "id";
    case 3:
      return "IP address";
    case 4:
      return "state";
    case 5:
      return "catch area";
    case 6:
      return "autonomy";
    case 7:
      return "clock";
    case 8:
      return "payload";
    case 9:
      return "quality";
    case 10:
      return "parent";
    default:
      return null;
    }
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    switch (columnIndex) {
    case 0:
      return Boolean.class;
    case 1:
      return Boolean.class;
    case 2:
      return Integer.class;
    case 3:
      return InetAddress.class;
    case 4:
      return SensorState.class;
    case 5:
      return CatchArea.class;
    case 6:
      return Integer.class;
    case 7:
      return Integer.class;
    case 8:
      return Integer.class;
    case 9:
      return Integer.class;
    case 10:
      return Integer.class;
    default:
      return null;
    }
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    if (columnIndex >= 4 && columnIndex <= 9) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void setValueAt(final Object value, final int rowIndex,
      final int columnIndex) {
    executor.submit(new Runnable() {
      @Override
      public void run() {
        int id = (Integer) getValueAt(rowIndex, 2);
        if (columnIndex == 4) {
          SensorState state = (SensorState) value;
          supervisor.setState(id, state);
        } else {
          CatchArea area = (CatchArea) getValueAt(rowIndex, 5);
          int autonomy = (Integer) getValueAt(rowIndex, 6);
          int clock = (Integer) getValueAt(rowIndex, 7);
          int payload = (Integer) getValueAt(rowIndex, 8);
          int quality = (Integer) getValueAt(rowIndex, 9);
          int parent = (Integer) getValueAt(rowIndex, 10);
          switch (columnIndex) {
          case 5:
            area = (CatchArea) value;
          case 6:
            autonomy = (Integer) value;
            break;
          case 7:
            clock = (Integer) value;
            break;
          case 8:
            payload = (Integer) value;
            break;
          case 9:
            quality = (Integer) value;
            break;
          }
          supervisor.setConf(id, new SensorConfiguration(area, autonomy, clock,
              payload, quality, parent));
        }
      }
    });
  }

  public void doDoubleClick(final int rowIndex, final int columnIndex) {
    if (columnIndex == 5) {
      final CatchArea area = showCatchAreaOptionPane((CatchArea) getValueAt(
          rowIndex, 5));
      if (area != null) {
        executor.submit(new Runnable() {
          @Override
          public void run() {
            int id = (Integer) getValueAt(rowIndex, 2);
            int autonomy = (Integer) getValueAt(rowIndex, 6);
            int clock = (Integer) getValueAt(rowIndex, 7);
            int payload = (Integer) getValueAt(rowIndex, 8);
            int quality = (Integer) getValueAt(rowIndex, 9);
            int parent = (Integer) getValueAt(rowIndex, 10);
            supervisor.setConf(id, new SensorConfiguration(area, autonomy,
                clock, payload, quality, parent));
          }
        });
      }
    }
  }

  private static CatchArea showCatchAreaOptionPane(CatchArea currentCatchArea) {

    String answer = JOptionPane.showInputDialog(null,
        "Enter a new catch area (4 integers separated with comma)",
        currentCatchArea.getP1().getX() + "," + currentCatchArea.getP1().getY()
            + "," + currentCatchArea.getP2().getX() + ","
            + currentCatchArea.getP1().getY());

    if (answer == null) {
      return null;
    }

    String[] tokens = answer.split(",");
    if (tokens.length < 4) {
      return null;
    }

    try {
      return new CatchArea(Integer.parseInt(tokens[0]), Integer
          .parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer
          .parseInt(tokens[3]));
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
