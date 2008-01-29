/**
 * 
 */
package fr.umlv.irsensor.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author sebastienmouret
 * 
 */
public class MyLogger {
  public static final String loggerName = "logger";
  public static final String loggerFileName = "IRSensor.log";

  static {
    Logger logger = Logger.getLogger(loggerName);
    FileHandler fh;

    try {

      // This block configure the logger with handler and formatter
      fh = new FileHandler("IrSensor.log", true);
      logger.addHandler(fh);
      logger.setLevel(Level.ALL);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);
      logger.log(Level.WARNING, "Logger started");
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
