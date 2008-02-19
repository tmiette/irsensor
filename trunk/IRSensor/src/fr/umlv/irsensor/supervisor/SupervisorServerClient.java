package fr.umlv.irsensor.supervisor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.irsensor.common.packets.PacketFactory;
import fr.umlv.irsensor.sensor.CatchArea;
import fr.umlv.irsensor.sensor.CatchArea.Point;

/**
 * SupervisorClient is a network client which is used to retrieve 
 * informations from sensors's server 
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class SupervisorServerClient {
	
	public SupervisorServerClient() {
	}
	
	public void setConf(int id, InetAddress ipAddress){
		//set a new configuration
		try {
			SocketChannel socketClient;
			socketClient = SocketChannel.open();
			System.out.println("Server is trying to reach a client...");
			socketClient.connect(new InetSocketAddress(ipAddress, 31000));
			
			socketClient.write(PacketFactory.createSetConfPacket(id, new CatchArea(new Point(0,0), new Point(0,0)), 
					0, 0, 0, 0, new byte[3]));

			final ByteBuffer buffer = ByteBuffer.allocate(64);
			socketClient.read(buffer);
			//wait for ack
			socketClient.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
