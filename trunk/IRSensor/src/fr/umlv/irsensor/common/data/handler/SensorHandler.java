package fr.umlv.irsensor.common.data.handler;

public interface SensorHandler {

	public byte[] dataToByteArray(Object data, String name);
	
	public Object byteArrayToData(byte[] bytes);
	
	public Object mergeData(Object... datas);
	
}
