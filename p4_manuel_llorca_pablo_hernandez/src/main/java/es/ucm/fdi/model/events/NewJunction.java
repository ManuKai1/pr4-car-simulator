package es.ucm.fdi.model.events;

import es.ucm.fdi.ini.IniError;
import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public class NewJunction extends Event{

	private String id;
	
	public NewJunction(int newTime, String ID) throws IniError{
		super(newTime);
		id = ID;	
	}
	
	@Override
	public void execute(TrafficSimulation sim) {
		
	}
	
	public Junction createJunction(){
		return new Junction(id);
	}

}
