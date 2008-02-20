package fr.umlv.irsensor.supervisor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import fr.umlv.irsensor.common.CatchArea;
import fr.umlv.irsensor.supervisor.exception.ParsingConfigurationException;

public class ConfigurationBuilder {

	private static final int NBR_PARAMETERS = 7; 
	
	private final int nbrSensors;
	
	private final List<SensorNode> sensors;
	
	public static ConfigurationBuilder load(File confFile) throws FileNotFoundException, IOException, ParsingConfigurationException{
		
		//load configuration file
		Properties properties = new Properties();
		properties.load(new FileInputStream(confFile));
		
		final int nbrSensors = Integer.valueOf((String)properties.get("NBR_SENSOR"));
		final ArrayList<SensorNode> sensors = new ArrayList<SensorNode>();
		for(int i=0; i<nbrSensors; i++){
			String sensorConf = (String)properties.get("SENSOR["+i+"]");
			String[] parameters = sensorConf.split(":");
			if(parameters.length != NBR_PARAMETERS){
				throw new ParsingConfigurationException("Invalid number of parameters");
			}
			final int id = Integer.valueOf(parameters[0]);
			
			String[] cArea = ((String)parameters[1].subSequence(1, parameters[1].length()-1)).split(",");
			final CatchArea area = new CatchArea(Integer.valueOf(cArea[0]), Integer.valueOf(cArea[1]),
										 			Integer.valueOf(cArea[2]), Integer.valueOf(cArea[3]));
			final int clock = Integer.valueOf(parameters[2]);
			final int autonomy = Integer.valueOf(parameters[3]);
			final int quality = Integer.valueOf(parameters[4]);
			final int payload = Integer.valueOf(parameters[5]);
			final int root = Integer.valueOf(parameters[6]);
			
			final SensorNode sensor = new SensorNode(id);
			sensor.setAutonomy(autonomy);
			sensor.setCArea(area);
			sensor.setIdParent(root);
			sensor.setPayload(payload);
			sensor.setQuality(quality);
			sensor.setClock(clock);
			
			sensors.add(sensor);
		}
		
		return new ConfigurationBuilder(nbrSensors, sensors);
	}
	
	private ConfigurationBuilder(int nbrSensors, List<SensorNode> sensors){
		this.nbrSensors = nbrSensors;
		this.sensors = sensors;
	}
	
	public int getNbrSensors(){
		return this.nbrSensors;
	}
	
	public List<SensorNode> getSensorsNode(){
		return this.sensors;
	}
}
