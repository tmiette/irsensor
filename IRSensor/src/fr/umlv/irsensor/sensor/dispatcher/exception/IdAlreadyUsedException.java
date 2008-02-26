package fr.umlv.irsensor.sensor.dispatcher.exception;

public class IdAlreadyUsedException extends Exception {
	
	public IdAlreadyUsedException(){
		super("The given id is already used");
	}
	
	public IdAlreadyUsedException(String errorMessage) {
		super(errorMessage);
	}
}
