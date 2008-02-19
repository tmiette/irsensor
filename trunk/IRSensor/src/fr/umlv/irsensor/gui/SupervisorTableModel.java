package fr.umlv.irsensor.gui;

import java.net.InetAddress;

import javax.swing.table.AbstractTableModel;

import fr.umlv.irsensor.sensor.CatchArea;
import fr.umlv.irsensor.sensor.SensorState;
import fr.umlv.irsensor.supervisor.SensorNode;
import fr.umlv.irsensor.supervisor.Supervisor;

public class SupervisorTableModel extends AbstractTableModel {

  private static final long serialVersionUID = -6940960745068220341L;

  private final Supervisor supervisor;

  public SupervisorTableModel(Supervisor supervisor) {
    this.supervisor = supervisor;
  }

  @Override
  public int getColumnCount() {
    return 9;
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
      return sensor.getId();
    case 1:
      return sensor.getAddress();
    case 2:
      return sensor.getState();
    case 3:
      return sensor.getCArea();
    case 4:
      return sensor.getAutonomy();
    case 5:
      return sensor.getClock();
    case 6:
      return sensor.getPayload();
    case 7:
      return sensor.getQuality();
    case 8:
      return sensor.getIdParent();
    default:
      return null;
    }
  }

  @Override
  public String getColumnName(int column) {
    switch (column) {
    case 0:
      return "id";
    case 1:
      return "IP address";
    case 2:
      return "state";
    case 3:
      return "catch area";
    case 4:
      return "autonomy";
    case 5:
      return "clock";
    case 6:
      return "payload";
    case 7:
      return "quality";
    case 8:
      return "parent";
    default:
      return null;
    }
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    switch (columnIndex) {
    case 0:
      return Integer.class;
    case 1:
      return InetAddress.class;
    case 2:
      return SensorState.class;
    case 3:
      return CatchArea.class;
    case 4:
      return Integer.class;
    case 5:
      return Integer.class;
    case 6:
      return Integer.class;
    case 7:
      return Integer.class;
    case 8:
      return Integer.class;
    default:
      return null;
    }
  }

}
