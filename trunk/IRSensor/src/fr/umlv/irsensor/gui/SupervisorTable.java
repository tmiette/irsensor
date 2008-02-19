package fr.umlv.irsensor.gui;

import javax.swing.JTable;

public class SupervisorTable {

  private final JTable table;

  public SupervisorTable(SupervisorTableModel model) {
    this.table = new JTable(model);
  }

  public JTable getJTable() {
    return this.table;
  }

}
