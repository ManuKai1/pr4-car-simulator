package es.ucm.fdi.model.events;

import es.ucm.fdi.ini.IniError;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public abstract class Event {
	
	private int time;
	
	public Event(int newTime){
		time = newTime;
	}

	public abstract void execute(TrafficSimulation sim);

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
}
