package fr.umlv.irsensor.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;

import fr.umlv.irsensor.supervisor.ConfigurationBuilder;
import fr.umlv.irsensor.supervisor.Supervisor;
import fr.umlv.irsensor.supervisor.SupervisorServer;
import fr.umlv.irsensor.supervisor.SupervisorServerClient;
import fr.umlv.irsensor.supervisor.exception.ParsingConfigurationException;

public class MainFrame {

  private final JTabbedPane mainContainer;
  private JFileChooser fileChooser;
  private Supervisor supervisor;

  public MainFrame() {
    final JTabbedPane pane = new JTabbedPane(JTabbedPane.BOTTOM);
    this.mainContainer = pane;
  }

  public JMenuBar createMenuBar() {
    final JMenuBar bar = new JMenuBar();
    final JMenu fileMenu = new JMenu("File");
    final JMenuItem loadConfigItem = new JMenuItem("Load");
    loadConfigItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        File f = selectConfigFile();

        if (f != null) {
          ConfigurationBuilder builder = null;
          try {
            builder = ConfigurationBuilder.load(f);
          } catch (ParsingConfigurationException e2) {
            System.err.println(e2.getMessage());
          } catch (FileNotFoundException e2) {
            System.err.println(e2.getMessage());
          } catch (IOException e2) {
            System.err.println(e2.getMessage());
          }

          supervisor = new Supervisor(builder.getSensorsNode(),
              new SupervisorServerClient(), new SupervisorServer());

          createPanel();

        }
      }
    });
    final JMenuItem quitItem = new JMenuItem("Quit");
    quitItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }

    });
    fileMenu.add(loadConfigItem);
    fileMenu.add(new JSeparator());
    fileMenu.add(quitItem);
    bar.add(fileMenu);
    return bar;
  }

  private File selectConfigFile() {

    if (this.fileChooser == null) {
      // initialize first file chooser
      JFileChooser chooser = new JFileChooser(".");
      chooser.setMultiSelectionEnabled(false);
      chooser.setDialogTitle("Choose a configuration file.");
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setAcceptAllFileFilterUsed(false);
      this.fileChooser = chooser;
    }

    // restart file chooser with the same directory as previous
    this.fileChooser
        .setCurrentDirectory(this.fileChooser.getCurrentDirectory());

    // ask for a directory
    if (this.fileChooser.showDialog(null, "Launch") == JFileChooser.APPROVE_OPTION) {
      return this.fileChooser.getSelectedFile();
    } else {
      return null;
    }

  }

  private void createPanel() {
    this.mainContainer.addTab("Sensors", new JScrollPane(new SupervisorTable(
        new SupervisorTableModel(this.supervisor)).getJTable()));
    this.mainContainer.addTab("Request", new JScrollPane());
  }

  public void launch() {
    final JFrame frame = new JFrame("Supervisor");
    frame.setSize(new Dimension(800, 600));
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setJMenuBar(createMenuBar());
    frame.setContentPane(this.mainContainer);
    frame.setVisible(true);
  }

}