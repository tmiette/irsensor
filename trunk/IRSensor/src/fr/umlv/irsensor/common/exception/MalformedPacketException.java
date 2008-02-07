package fr.umlv.irsensor.common.exception;

public class MalformedPacketException extends Exception {
	
	public MalformedPacketException(){
		super("The given packet is malformed");
	}
	
	public MalformedPacketException(String errorMessage) {
		super(errorMessage);
	}
	
	
}
