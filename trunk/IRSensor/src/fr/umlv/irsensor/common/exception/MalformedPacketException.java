package fr.umlv.irsensor.common.exception;

public class MalformedPacketException extends Exception {

  private static final long serialVersionUID = 8189737281515651765L;

  public MalformedPacketException() {
    super("The given packet is malformed.");
  }

  public MalformedPacketException(String errorMessage) {
    super(errorMessage);
  }

}
