package fr.umlv.irsensor.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import fr.umlv.irsensor.common.fields.SensorState;

public class SupervisorTable {

  private final JPanel mainPanel;

  public SupervisorTable(final SupervisorTableModel model) {
    final JTable table = new JTable(model);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    table.setDefaultRenderer(Boolean.class, new DefaultTableCellRenderer() {

      private static final long serialVersionUID = -1020049183063716872L;

      @Override
      public Component getTableCellRendererComponent(JTable table,
          Object value, boolean isSelected, boolean hasFocus, int row,
          int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
            row, column);

        setText("");
        if (((Boolean) value) == true) {
          setBackground(Color.GREEN);
        } else {
          setBackground(Color.RED);
        }

        return this;
      }

    });

    table.setDefaultRenderer(SensorState.class, new DefaultTableCellRenderer() {

      private static final long serialVersionUID = -8647357996667453097L;

      @Override
      public Component getTableCellRendererComponent(JTable table,
          Object value, boolean isSelected, boolean hasFocus, int row,
          int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
            row, column);

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
    table.getColumnModel().getColumn(4).setCellEditor(
        new DefaultCellEditor(stateCombo));

    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() >= 2) {
          int row = table.rowAtPoint(e.getPoint());
          int column = table.columnAtPoint(e.getPoint());
          if (row != -1 && column != -1) {
            model.doDoubleClick(row, column);
          }
        }
      }
    });

    final JPanel buttonPanel = new JPanel();
    final JButton buildButton = new JButton("Build network");
    buildButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        model.buildNetworkConfiguration();
      }
    });
    buttonPanel.add(buttonPanel);

    this.mainPanel = new JPanel(new BorderLayout());
    this.mainPanel.add(table, BorderLayout.CENTER);
    this.mainPanel.add(buttonPanel, BorderLayout.SOUTH);

  }

  public JPanel getMainPanel() {
    return this.mainPanel;
  }

}
