package fr.umlv.irsensor.supervisor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.supervisor.exception.ParsingConfigurationException;
import fr.umlv.irsensor.util.Pair;

public class ConfigurationBuilder {

  private static final int NBR_PARAMETERS = 7;

  private final int nbrSensors;

  private final List<Pair<Integer, SensorConfiguration>> confs;

  public static ConfigurationBuilder load(File confFile)
      throws FileNotFoundException, IOException, ParsingConfigurationException {

    // load configuration file
    Properties properties = new Properties();
    properties.load(new FileInputStream(confFile));

    final int nbrSensors = Integer.valueOf((String) properties
        .get("NBR_SENSOR"));
    final ArrayList<Pair<Integer, SensorConfiguration>> confs = new ArrayList<Pair<Integer, SensorConfiguration>>();
    for (int i = 0; i < nbrSensors; i++) {
      String sensorConf = (String) properties.get("SENSOR[" + i + "]");
      String[] parameters = sensorConf.split(":");
      if (parameters.length != NBR_PARAMETERS) {
        throw new ParsingConfigurationException("Invalid number of parameters");
      }
      final int id = Integer.valueOf(parameters[0]);

      String[] cArea = ((String) parameters[1].subSequence(1, parameters[1]
          .length() - 1)).split(",");
      final CatchArea area = new CatchArea(Integer.valueOf(cArea[0]), Integer
          .valueOf(cArea[1]), Integer.valueOf(cArea[2]), Integer
          .valueOf(cArea[3]));
      final int clock = Integer.valueOf(parameters[2]);
      final int autonomy = Integer.valueOf(parameters[3]);
      final int quality = Integer.valueOf(parameters[4]);
      final int payload = Integer.valueOf(parameters[5]);
      final int root = Integer.valueOf(parameters[6]);

      final SensorConfiguration conf = new SensorConfiguration(area, autonomy,
          clock, payload, quality, root);

      confs.add(new Pair<Integer, SensorConfiguration>(id, conf));
    }

    return new ConfigurationBuilder(nbrSensors, confs);
  }

  private ConfigurationBuilder(int nbrSensors,
      List<Pair<Integer, SensorConfiguration>> confs) {
    this.nbrSensors = nbrSensors;
    this.confs = confs;
  }

  public int getNbrSensors() {
    return this.nbrSensors;
  }

  public List<Pair<Integer, SensorConfiguration>> getSensorsConfigurations() {
    return this.confs;
  }
}
