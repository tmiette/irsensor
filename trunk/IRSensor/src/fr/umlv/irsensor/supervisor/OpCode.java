package fr.umlv.irsensor.supervisor;

public enum OpCode {

  REQCON("00000000"),
  REPCON("00000001"),
  SETCONF("00000010"),
  GETCONF("00000011"),
  REPCONF("00000100"),
  SETSTA("00000101"),
  GETSTA("00000110"),
  REPSTA("00000111"),
  ACK("00001000"),
  REQDATA("00001001"),
  REPDATA("00001010");

  private final Byte code;

  private OpCode(String code) {
    this.code = new Byte(code);
  }

  public Byte getCode() {
    return this.code;
  }

}
