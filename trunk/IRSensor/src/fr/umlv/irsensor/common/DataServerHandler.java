package fr.umlv.irsensor.common;

import fr.umlv.irsensor.common.fields.CatchArea;

public interface DataServerHandler {
	
	public Object getSubData(CatchArea area);
	
	public byte[] dataToByteArray(Object data, String name);
	
	public Object reduceData(Object data);
}