package fr.umlv.irsensor.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.SequentialGroup;

import fr.umlv.irsensor.common.data.MimeTypes;
import fr.umlv.irsensor.common.data.MimetypeException;
import fr.umlv.irsensor.common.data.handler.SensorHandlers;
import fr.umlv.irsensor.common.fields.CatchArea;

public class RequestPanel {

  private final JPanel mainPanel;
  private final RequestModel model;
  private final JLabel imageLabel;
  private final JTextField area1XField;
  private final JTextField area1YField;
  private final JTextField area2XField;
  private final JTextField area2YField;

  public RequestPanel(RequestModel model) {
    this.model = model;

    final JLabel areaLabel = new JLabel("Catch area :");
    final JLabel qualityLabel = new JLabel("Quality :");
    final JLabel clockLabel = new JLabel("Clock :");
    this.area1XField = new JTextField("0");
    this.area1YField = new JTextField("0");
    this.area2XField = new JTextField("500");
    this.area2YField = new JTextField("500");
    final JTextField qualityField = new JTextField("15");
    final JTextField clockField = new JTextField("0");
    final JButton submit = new JButton("Submit");
    submit.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (area1XField.getText() != null && area1YField.getText() != null
            && area2XField.getText() != null && area2YField.getText() != null) {
          try {
            final int quality = qualityField.getText() == "" ? 5 : Integer
                .valueOf(qualityField.getText());
            final int clock = clockField.getText() == "" ? 0 : Integer
                .valueOf(clockField.getText());
            final int area1X = Integer.valueOf(area1XField.getText());
            final int area1Y = Integer.valueOf(area1YField.getText());
            final int area2X = Integer.valueOf(area2XField.getText());
            final int area2Y = Integer.valueOf(area2YField.getText());

            if (area1X < 0 || area1Y < 0 || area2X < 0 || area2Y < 0)
              throw new NumberFormatException();
            RequestPanel.this.model.submitRequest(new CatchArea(area1X, area1Y,
                area2X, area2Y), quality, clock);
          } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(null, "Invalid Format Number",
                "Error", JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    });

    this.model.addRequestListener(new RequestListener() {

      @Override
      public void answerReceived(byte[] data) {
        BufferedImage im = null;
        try {
          im = (BufferedImage) SensorHandlers.byteArrayToData(data,
              MimeTypes.IMAGE_PNG);
        } catch (MimetypeException e) {
          e.printStackTrace();
        }

        int x = Integer.parseInt(area1XField.getText());
        int y = Integer.parseInt(area1YField.getText());
        int w = Integer.parseInt(area2XField.getText()) - x;
        int h = Integer.parseInt(area2YField.getText()) - y;
        ImageIcon icon = new ImageIcon(im.getSubimage(x, y, w, h));
        // ImageIcon icon = new ImageIcon(data);
        imageLabel.setText(null);
        imageLabel.setIcon(icon);
      }

    });

    final JPanel requestPanel = new JPanel(null);
    requestPanel.setBorder(BorderFactory
        .createTitledBorder("Submit a data request :"));
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
        .addComponent(qualityLabel).addComponent(qualityField));
    vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        .addComponent(clockLabel).addComponent(clockField));
    vGroup.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        .addComponent(submit));
    layout.setVerticalGroup(vGroup);

    final JPanel answerPanel = new JPanel();
    answerPanel.setBorder(BorderFactory.createTitledBorder("Response :"));
    this.imageLabel = new JLabel("image");
    answerPanel.add(this.imageLabel);

    this.mainPanel = new JPanel(new BorderLayout());
    this.mainPanel.add(requestPanel, BorderLayout.NORTH);
    this.mainPanel.add(answerPanel, BorderLayout.CENTER);
  }

  public JPanel getMainPanel() {
    return this.mainPanel;
  }

}
