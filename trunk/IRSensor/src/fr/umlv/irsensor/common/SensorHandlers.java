package fr.umlv.irsensor.common;

import java.util.HashMap;

public class SensorHandlers {

	
	private static final HashMap<MimeTypes, SensorHandler> handlers = new HashMap<MimeTypes, SensorHandler>();
	
	static {
		handlers.put(MimeTypes.IMAGE, new SensorImageHandler());
	}
	
	private static SensorHandler ensureHandlerExists(MimeTypes mimeType) throws MimetypeException {
		SensorHandler handler = handlers.get(mimeType);
		if (handler == null) throw new MimetypeException("No mimetype correspondind to data "+mimeType);
		return handler;
	}
	
	public static byte[] dataToByteArray(Object data, MimeTypes mimeType, String name) throws MimetypeException {
		SensorHandler handler = ensureHandlerExists(mimeType);
		return handler.dataToByteArray(data, name);
	}

	public static Object byteArrayToData(byte[] bytes, MimeTypes mimeType) throws MimetypeException{
		SensorHandler handler = ensureHandlerExists(mimeType);
		return handler.byteArrayToData(bytes);
	}

	public static Object mergeData(MimeTypes mimeType, Object... datas) throws MimetypeException {
		SensorHandler handler = ensureHandlerExists(mimeType);
		return handler.mergeData(datas);
	}
}
