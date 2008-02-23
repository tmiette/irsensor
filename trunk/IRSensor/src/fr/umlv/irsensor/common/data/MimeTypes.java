package fr.umlv.irsensor.common.data;

public enum MimeTypes {
  IMAGE(0, "image"),
  TEXT(1, "text");

  private final int id;

  private final String name;

  private MimeTypes(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public static MimeTypes getMimeType(int id) throws MimetypeException {
    for (MimeTypes mime : MimeTypes.values()) {
      if (mime.id == id) {
        return mime;
      }
    }
    throw new MimetypeException("The type mime with id " + id
        + " doesn't exist.");
  }

  public static MimeTypes getMimeType(String name) throws MimetypeException {
    for (MimeTypes mime : MimeTypes.values()) {
      if (mime.name.equals(name)) {
        return mime;
      }
    }
    throw new MimetypeException("The type mime with name " + name
        + " doesn't exist.");
  }
}
