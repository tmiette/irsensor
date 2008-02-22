package fr.umlv.irsensor.common;

import fr.umlv.irsensor.common.fields.CatchArea;

public interface DataServerHandler<T> {
	
	public T getSubData(CatchArea area);
	
	public T reduceData(T data);
}