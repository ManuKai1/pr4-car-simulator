package es.ucm.fdi.control.evbuild;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.events.Event;

public abstract class EventBuilder {

	abstract Event parse(IniSection ini);
	
}
