package es.ucm.fdi.model.events;

import java.util.ArrayList;

import es.ucm.fdi.model.simulation.NonExistingSimObjException;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public class FaultyVehicle extends Event {

	private ArrayList<String> vehiclesID;
	private int duration;
	
	public FaultyVehicle(int newTime, ArrayList<String> vID, int dur) {
		super(newTime);
		vehiclesID = vID;
		duration = dur;
	}
	
	@Override
	public void execute(TrafficSimulation sim) {
		try {
			sim.makeFaulty(vehiclesID, duration);
		}
		catch (NonExistingSimObjException e) {
			System.err.println(e.getMessage());
		}
	}
}
