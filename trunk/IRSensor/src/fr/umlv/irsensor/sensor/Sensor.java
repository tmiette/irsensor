package fr.umlv.irsensor.sensor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.umlv.irsensor.common.SensorConfiguration;
import fr.umlv.irsensor.common.exception.MalformedPacketException;
import fr.umlv.irsensor.common.fields.CatchArea;
import fr.umlv.irsensor.common.fields.SensorState;
import fr.umlv.irsensor.sensor.dispatcher.PacketDispatcher;
import fr.umlv.irsensor.sensor.dispatcher.exception.IdAlreadyUsedException;
import fr.umlv.irsensor.sensor.networkClients.DataClient;
import fr.umlv.irsensor.sensor.networkClients.SensorClient;
import fr.umlv.irsensor.sensor.networkClients.SupervisorClient;
import fr.umlv.irsensor.sensor.networkServer.SensorServer;
import fr.umlv.irsensor.sensor.networkServer.SupervisorSensorServer;
import fr.umlv.irsensor.util.Pair;

public class Sensor {

	private SensorConfiguration conf;

	private int id;

	private SensorState state = SensorState.DOWN;

	private final SupervisorClient supervisorClient;
	private DataClient dataClient;
	private SensorClient sensorClient;
	private SupervisorSensorServer supervisorServer;
	private SensorServer sensorServer;

	private final String supervisorServerAddr;
	private final String dataServerAddr;

	private final ArrayList<Pair<Integer, InetAddress>> children;

	private final ExecutorService executor = Executors.newFixedThreadPool(2);

	private int nbrOfRepData = 0;

	public Sensor(final PacketDispatcher supervisorServer,
			final PacketDispatcher sensorServer, String dataServerAddr,
			String supervisorServerAddr) throws IOException {
		this.dataServerAddr = dataServerAddr;
		this.supervisorServerAddr = supervisorServerAddr;
		this.supervisorClient = new SupervisorClient(this);

		try {
			this.supervisorClient.registrySensor();
			System.out.println("id recu " + this.id);
			this.supervisorServer = new SupervisorSensorServer(this.id);
			this.supervisorServer.addSupervisorSensorListener(new SupervisorSensorListener() {
				@Override
				public void reqDataReceived(final CatchArea area, final int clock,
						final int quality) {
					if(children.size()>0){
						sendReqData(area,clock, quality);
					}
					else{
						sendRepData();
					}
				}

				@Override
				public void confReceived(SensorConfiguration conf) {
					if (Sensor.this.conf == null
							|| state.equals(SensorState.PAUSE)
							|| state.equals(SensorState.UP)) {
						Sensor.this.conf = conf;
						System.out.println("New conf received.");
					}
				}

				@Override
				public void stateChanged(SensorState state) {
					if (Sensor.this.conf != null) {
						Sensor.this.state = state;
						switch (state) {
						case DOWN:
						case PAUSE:
							stopSensorServer();
							stopSensorClient();
							stopDataClient();
							break;
						case UP:
							startSensorServer(sensorServer, id);
							startSensorClient(conf.getParentId(), id,
									conf.getParentAddress());
							startDataClient();
							break;
						default:
							break;
						}
					}
				}
			});

			supervisorServer.register(this.supervisorServer);
		} catch (MalformedPacketException e) {
			e.printStackTrace();
		} catch (IdAlreadyUsedException e) {
			e.printStackTrace();
		}

		this.children = new ArrayList<Pair<Integer, InetAddress>>();
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public SensorConfiguration getConfiguration() {
		return this.conf;
	}

	public SupervisorClient getSupervisorClient() {
		return this.supervisorClient;
	}

	private void startSensorServer(PacketDispatcher dispatcher, final int id) {
		this.sensorServer = new SensorServer(id);
		this.sensorServer.addSensorServerListener(new SensorServerListener() {
			@Override
			public void helloRequestReceived(int id, InetAddress address) {
				System.out.println("Hello request receveid to "
						+ Sensor.this.id + " from " + id
						+ ". Add it to my children list.");
				children.add(new Pair<Integer, InetAddress>(id, address));
			}

			@Override
			public void reqDataReceived(CatchArea area, int clock, int quality) {
				System.out.println(Sensor.this.id + " receive a req data");
				if(children.size()>0){
					sendReqData(area, clock, quality);
				}
				else{
					sendRepData();
				}
			}

			@Override
			public void repDataReceived(byte[] data, int mimeType) {
				nbrOfRepData++;
				if(nbrOfRepData == children.size()){
					sendRepData();
				}
			}
		});
		try {
			dispatcher.register(Sensor.this.sensorServer);
		} catch (IdAlreadyUsedException e) {
			e.printStackTrace();
		}
	}

	private void startSensorClient(int idD, int idS, InetAddress address) {
		this.sensorClient = new SensorClient();
		this.sensorClient.addSensorClientListener(new SensorClientListener() {
			@Override
			public void helloReplyReceived() {
				// do nothing, my father is correctly connected
				System.out.println("Hello reply receveid to " + Sensor.this.id);
			}
		});
		this.sensorClient.sendHelloRequest(idD, idS, address);
	}

	private void stopSensorServer() {
		// TODO
	}

	private void stopSensorClient() {
		// TODO
	}

	private void startDataClient() {
		this.dataClient = new DataClient(this.dataServerAddr);
		this.dataClient.addSensorDataListener(new SensorDataListener() {
			@Override
			public void dataReceived(byte[] data) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void stopDataClient() {
		// TODO
	}

	private void sendReqData(final CatchArea cArea, final int clock, final int quality){
		if (conf != null && state.equals(SensorState.UP)) {
			for (final Pair<Integer, InetAddress> pair : Sensor.this.children) {
				Sensor.this.executor.submit(new Runnable(){
					@Override
					public void run() {
						Sensor.this.sensorClient.sendReqData(pair
								.getSecondElement(), pair
								.getFirstElement(), cArea, clock,
								quality);
					};
				});
			}
		}
	}

	private void sendRepData(){
		nbrOfRepData = 0;
		System.out.println("Send rep data "+this.id);
		if(this.conf.getParentId() == -1){
			//this.supervisorClient.sendRepData(this.conf.getParentAddress(), this.id);
			System.out.println("reponse finale");
		}
		else{
			this.sensorClient.sendRepData(this.conf.getParentAddress(), this.conf.getParentId());
		}
	}

}
