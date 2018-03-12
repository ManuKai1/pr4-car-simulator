package es.ucm.fdi.model.events;

import es.ucm.fdi.model.SimObj.Road;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public class NewRoad extends Event {
	
	private String id;
	private String src;
	private String dest;
	private int maxSpeed;
	private int length;

	public NewRoad(int newTime, String ID, String source, String destination, int max, int lgth){
		super(newTime);
		id = ID;
		src = source;
		dest = destination;
		maxSpeed = max;
		length = lgth;
	}
	
	@Override
	public void execute(TrafficSimulation sim) {
		

	}

	public Road createRoad(){
		//Búsqueda de junctions origen y destino
		return new Road(id, srcJunction, destJunction, maxSpeed, length);
	}
	
}
