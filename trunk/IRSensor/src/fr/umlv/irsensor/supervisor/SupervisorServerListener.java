package fr.umlv.irsensor.supervisor;

import java.net.InetAddress;

import fr.umlv.irsensor.common.packets.ErrorCode;

public interface SupervisorServerListener {
	
	public void ErrorCodeReceived(ErrorCode code);
	
	public void ReqConPacketReceived(int id, InetAddress ipAddress);
	
	public void registrationTerminated();
}
