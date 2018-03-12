package es.ucm.fdi.model.events;

import java.util.ArrayList;

import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.SimObj.Vehicle;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public class NewVehicle extends Event {

	private String id;
	private int maxSpeed;
	private ArrayList<String> itinerary;
	
	public NewVehicle(int newTime, String ID, int max, ArrayList<String> junctions){
		super(newTime);
		id = ID;
		maxSpeed = max;
		itinerary = junctions;
	}
	
	@Override
	public void execute(TrafficSimulation sim) {
		

	}
	
	public Vehicle createVehicle(){
		//Búsqueda de itinerario por array de ids.
		return new Vehicle(id, maxSpeed, jItinerary);
	}

}
