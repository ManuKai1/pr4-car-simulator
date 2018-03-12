package es.ucm.fdi.model.events;

import java.util.ArrayList;

import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public class NewJunction extends Event{

	private String id;
	
	public NewJunction(int newTime, String ID){
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
