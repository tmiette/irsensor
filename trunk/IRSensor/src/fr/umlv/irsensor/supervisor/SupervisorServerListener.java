package fr.umlv.irsensor.supervisor;

import java.net.InetAddress;

import fr.umlv.irsensor.common.fields.ErrorCode;

public interface SupervisorServerListener {
	
	public void ErrorCodeReceived(ErrorCode code);
	
	public void ackConPacketReceived(int id, InetAddress ipAddress);
	
	public void registrationTerminated();
}
