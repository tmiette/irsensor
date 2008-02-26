package fr.umlv.irsensor.common.data.handler;

import fr.umlv.irsensor.common.data.MimeTypes;
import fr.umlv.irsensor.common.fields.CatchArea;

public interface DataServerHandler {
	
	public Object getSubData(CatchArea area);
	
	public byte[] dataToByteArray(Object data);
	
	public MimeTypes getMimeType();

}