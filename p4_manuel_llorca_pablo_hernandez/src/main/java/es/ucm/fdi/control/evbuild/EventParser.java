package es.ucm.fdi.control.evbuild;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.events.Event;

public class EventParser {

	//Array con posibles eventos
	private static EventBuilder[] events = 
		{
			new FaultyVehicleBuilder(), 
			new NewJunctionBuilder(), 
			new NewRoadBuilder(), 
			new NewVehicleBuilder(),
		};
	
	//Función de búsqueda de evento
	public Event parse(IniSection ini) throws IllegalArgumentException{
		try{
			for(EventBuilder event : events) {
				Event next = event.parse(ini);
				if(next != null) return next;
			}
		}
		catch(IllegalArgumentException e){
			throw e;
		}
		//Si llegamos a este punto es que todos los parse han devuelto null
		throw new IllegalArgumentException("No event found.");
	}
	
	
}
