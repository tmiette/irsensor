package fr.umlv.irsensor.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import fr.umlv.irsensor.common.CatchArea;
import fr.umlv.irsensor.common.SensorState;

public class SupervisorTable {

  private final JTable table;

  public SupervisorTable(SupervisorTableModel model) {
    this.table = new JTable(model);
    this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    this.table.setDefaultRenderer(Boolean.class,
        new DefaultTableCellRenderer() {

          private static final long serialVersionUID = -1020049183063716872L;

          @Override
          public Component getTableCellRendererComponent(JTable table,
              Object value, boolean isSelected, boolean hasFocus, int row,
              int column) {
            super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);

            setText("");
            if (((Boolean) value) == true) {
              setBackground(Color.GREEN);
            } else {
              setBackground(Color.RED);
            }

            return this;
          }

        });

    this.table.setDefaultRenderer(SensorState.class,
        new DefaultTableCellRenderer() {

          private static final long serialVersionUID = -8647357996667453097L;

          @Override
          public Component getTableCellRendererComponent(JTable table,
              Object value, boolean isSelected, boolean hasFocus, int row,
              int column) {
            super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);

            setFont(new Font(null, Font.BOLD, 12));
            if ((SensorState) value != null) {
              switch ((SensorState) value) {
              case DOWN:
                setForeground(Color.RED);
                break;
              case PAUSE:
                setForeground(Color.YELLOW);
                break;
              case UP:
                setForeground(Color.GREEN);
                break;
              default:
                break;
              }
            }
            return this;
          }

        });

    final JComboBox stateCombo = new JComboBox(SensorState.values());
    this.table.getColumnModel().getColumn(4).setCellEditor(
        new DefaultCellEditor(stateCombo));
    final JComboBox areaCombo = new JComboBox(new CatchArea[] { new CatchArea(
        0, 0, 0, 0) });
    this.table.getColumnModel().getColumn(5).setCellEditor(
        new DefaultCellEditor(areaCombo));

  }

  public JTable getJTable() {
    return this.table;
  }

}
