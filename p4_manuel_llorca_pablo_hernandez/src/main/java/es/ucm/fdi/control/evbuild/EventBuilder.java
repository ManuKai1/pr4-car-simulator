package es.ucm.fdi.control.evbuild;

import java.util.ArrayList;
import java.util.regex.Pattern;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.events.Event;

public abstract class EventBuilder {

	protected String iniName;
	
	public EventBuilder(String name){
		iniName = name;
	}
	
	abstract Event parse(IniSection ini);
	
	static boolean validID(String id){
		return Pattern.matches("\\w+", id);
	}
}
