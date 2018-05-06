package es.ucm.fdi.model.events;

import java.util.ArrayList;

import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.SimObj.Vehicle;
import es.ucm.fdi.model.simulation.AlreadyExistingSimObjException;
import es.ucm.fdi.model.simulation.NonExistingSimObjException;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public class NewVehicle extends Event {

	protected String id;
	protected int maxSpeed;
	protected ArrayList<String> tripID;
	
	public NewVehicle(int newTime, String ID, int max, ArrayList<String> junctions) {
		super(newTime);
		id = ID;
		maxSpeed = max;
		tripID = junctions;
	}
	
	public String getId() {
		return id;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public ArrayList<String> getTripID(){
		return tripID;
	}

	@Override
	public void execute(TrafficSimulation sim) throws NonExistingSimObjException, AlreadyExistingSimObjException {
		if ( ! sim.existsVehicle(getId()) ) {
			try {
				Vehicle newV = newVehicle(sim);
				sim.addVehicle( newV );
			}
			catch (NonExistingSimObjException e) {
				System.err.println( e.getMessage() );
			}
		}
		else {
			throw new AlreadyExistingSimObjException("Vehicle with id: " + getId() + " already in simulation.");
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
				throw new NonExistingSimObjException("Junction with id: " + jID + " from itinerary of vehicle with id: " + getId() + " not found in simulation.");
			}
		}
		return new Vehicle(getId(), trip, maxSpeed);
	}

	public boolean equals(Object obj){
		boolean same;
		same = super.equals(obj);
		if(same){
			NewVehicle other = (NewVehicle) obj;
			same = same && getId() == other.getId();
			same = same && maxSpeed == other.maxSpeed;
			same = same && tripID.equals(other.tripID);
		}
		return same;
	}
	
}
