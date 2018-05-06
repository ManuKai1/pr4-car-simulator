package es.ucm.fdi.model.events;

import java.util.ArrayList;

import es.ucm.fdi.model.SimObj.BikeVehicle;
import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.SimObj.Vehicle;
import es.ucm.fdi.model.simulation.AlreadyExistingSimObjException;
import es.ucm.fdi.model.simulation.NonExistingSimObjException;
import es.ucm.fdi.model.simulation.SimulationException;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public class NewBikeVehicle extends NewVehicle {

	public NewBikeVehicle(int newTime, String ID, int max,
			ArrayList<String> junctions) {
		super(newTime, ID, max, junctions);
	}

	public void execute(TrafficSimulation sim) throws NonExistingSimObjException, AlreadyExistingSimObjException{
		if ( ! sim.existsVehicle(getId()) ) {
			try {
				BikeVehicle newV = newBikeVehicle(sim);
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
	
	/**
	 * Función que genera una bicicleta a partir de sus datos
	 * @param sim Simulador
	 * @return BikeVehicle con los datos del Event
	 * @throws NonExistingSimObjException
	 */
	private BikeVehicle newBikeVehicle(TrafficSimulation sim) throws NonExistingSimObjException {
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
		return new BikeVehicle(getId(), trip, maxSpeed);
	}
	
}
