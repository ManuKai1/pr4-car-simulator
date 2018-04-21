package es.ucm.fdi.model.events;

import java.util.ArrayList;

import es.ucm.fdi.model.SimObj.CarVehicle;
import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.simulation.AlreadyExistingSimObjException;
import es.ucm.fdi.model.simulation.NonExistingSimObjException;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public class NewCarVehicle extends NewVehicle {
	
	//Resistencia a las averías
	private int resistance;
	//Probabilidad de avería
	private double faultyChance;
	//Duración máxima de avería
	private int faultDuration;
	//Semilla aleatoria
	private long randomSeed;
	
	public NewCarVehicle(int newTime, String ID, int max,
			ArrayList<String> junctions, int res, double breakChance, int breakDuration, long seed) {
		super(newTime, ID, max, junctions);
		resistance = res;
		faultyChance = breakChance;
		faultDuration = breakDuration;
		randomSeed = seed;
	}
	
	public void execute(TrafficSimulation sim) throws NonExistingSimObjException, AlreadyExistingSimObjException{
		if ( ! sim.existsVehicle(getId()) ) {
			try {
				CarVehicle newV = newCarVehicle(sim);
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
	
	private CarVehicle newCarVehicle(TrafficSimulation sim) throws NonExistingSimObjException {
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
		return new CarVehicle(id, trip, maxSpeed, resistance, faultyChance, faultDuration, randomSeed);
	}
	
}
