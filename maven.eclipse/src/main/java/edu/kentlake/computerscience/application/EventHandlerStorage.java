package edu.kentlake.computerscience.application;

import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class EventHandlerStorage {
	public static final String OPEN_FILE = "OpenFile";
	public static final String MAKE_FOLDER = "MakeFolder";

	private Map<String, EventHandler<ActionEvent>> storage;
	
	public EventHandlerStorage() {
		storage = new HashMap<>();
	}
	
	public EventHandler<ActionEvent> get(String eventName){
		return storage.get(eventName);
	}
	
	public void put(String eventName, EventHandler<ActionEvent> event) {
		storage.put(eventName, event);
	}
}

