package fr.umlv.irsensor.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.SequentialGroup;

public class RequestPanel {

  private final JPanel mainPanel;

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
    final JButton submit = new JButton("Submit");

    final JPanel requestPanel = new JPanel(null);
    requestPanel.setBorder(BorderFactory.createTitledBorder("Submit a data request :"));
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
    hGroup.addGroup(layout.createParallelGroup().addComponent(area2YField)
        .addComponent(submit));
    layout.setHorizontalGroup(hGroup);

    GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
    vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        .addComponent(areaLabel).addComponent(area1XField).addComponent(
            area1YField).addComponent(area2XField).addComponent(area2YField));
    vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        .addComponent(clockLabel).addComponent(qualityField));
    vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        .addComponent(qualityLabel).addComponent(clockField));
    vGroup.addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(
        submit));
    layout.setVerticalGroup(vGroup);

    final JPanel answerPanel = new JPanel();
    answerPanel.setBorder(BorderFactory.createTitledBorder("Response :"));
    answerPanel.add(new JLabel("image"));
    
    this.mainPanel = new JPanel(new BorderLayout());
    this.mainPanel.add(requestPanel, BorderLayout.NORTH);
    this.mainPanel.add(answerPanel, BorderLayout.CENTER);
  }

  public JPanel getMainPanel() {
    return this.mainPanel;
  }

}
