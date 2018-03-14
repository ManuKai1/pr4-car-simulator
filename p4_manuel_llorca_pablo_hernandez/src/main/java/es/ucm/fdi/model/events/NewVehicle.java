package es.ucm.fdi.model.events;

import java.util.ArrayList;

import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.SimObj.Vehicle;
import es.ucm.fdi.model.simulation.AlreadyExistingSimObjException;
import es.ucm.fdi.model.simulation.NonExistingSimObjException;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public class NewVehicle extends Event {

	private String id;
	private int maxSpeed;
	private ArrayList<String> tripID;
	
	public NewVehicle(int newTime, String ID, int max, ArrayList<String> junctions) {
		super(newTime);
		id = ID;
		maxSpeed = max;
		tripID = junctions;
	}
	
	@Override
	public void execute(TrafficSimulation sim) throws NonExistingSimObjException, AlreadyExistingSimObjException {
		if ( ! sim.existsVehicle(id) ) {
			try {
				Vehicle newV = newVehicle(sim);
				sim.addVehicle( newV );
			}
			catch (NonExistingSimObjException e) {
				System.err.println( e.getMessage() );
			}
		}
		else {
			throw new AlreadyExistingSimObjException("Vehicle with id: " + id + " already in simulation.");
		}
	}
	
	private Vehicle newVehicle(TrafficSimulation sim) throws NonExistingSimObjException {
		ArrayList<Junction> trip = new ArrayList<Junction>();

		// Deben existir todos los cruces del itinerario en el momento del evento.
		for ( String jID : tripID ) {
			Junction j = sim.getJunction(jID);
			if ( j != null ) {
				trip.add(j);
			}
			else {
				throw new NonExistingSimObjException("Junction with id: " + jID + " from itinerary of vehicle with id: " + id + " not found in simulation.");
			}
		}
		return new Vehicle(id, trip, maxSpeed);
	}

}
