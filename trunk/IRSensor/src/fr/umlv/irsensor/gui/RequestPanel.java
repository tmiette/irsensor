package fr.umlv.irsensor.gui;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JPanel;

public class RequestPanel {

  private final JPanel requestPanel;

  public RequestPanel() {

    final JLabel areaLabel = new JLabel("Catch area :");
    final JLabel qualityLabel = new JLabel("Quality :");
    final JLabel clockLabel = new JLabel("Clock :");
    final JTextField area1XField = new JTextField();
    final JTextField area1YField = new JTextField();
    final JTextField area2XField = new JTextField();
    final JTextField area2YField = new JTextField();
    final JTextField qualityField = new JTextField();
    final JTextField clockField = new JTextField();
    final JButton submit = new JButton("Submit request");

    this.requestPanel = new JPanel(null);
    final GroupLayout layout = new GroupLayout(requestPanel);
    requestPanel.setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);

    SequentialGroup hGroup = layout.createSequentialGroup();

    hGroup.addGroup(layout.createParallelGroup().addComponent(areaLabel)
        .addComponent(qualityLabel).addComponent(clockLabel));
    hGroup.addGroup(layout.createParallelGroup().addComponent(area1XField)
        .addComponent(qualityField).addComponent(clockField));
    hGroup.addGroup(layout.createParallelGroup().addComponent(area1YField));
    hGroup.addGroup(layout.createParallelGroup().addComponent(area2XField));
    hGroup.addGroup(layout.createParallelGroup().addComponent(area2YField));
    layout.setHorizontalGroup(hGroup);

    GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
    vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        .addComponent(areaLabel).addComponent(area1XField).addComponent(
            area1YField).addComponent(area2XField).addComponent(area2YField));
    vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        .addComponent(qualityLabel).addComponent(qualityField));
    vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        .addComponent(qualityLabel).addComponent(clockField));
    layout.setVerticalGroup(vGroup);

  }

  public JPanel getMainPanel() {
    return this.requestPanel;
  }

}
