package es.ucm.fdi.model.events;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.simulation.AlreadyExistingSimObjException;
import es.ucm.fdi.model.simulation.NonExistingSimObjException;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public abstract class Event {
	
	private int time;
	
	public Event(int newTime) {
		time = newTime;
	}

	public abstract void execute(TrafficSimulation sim) throws AlreadyExistingSimObjException, NonExistingSimObjException;

	public int getTime() {
		return time;
	}
	
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		return time == other.getTime();
	}
	
}
