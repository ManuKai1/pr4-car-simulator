package es.ucm.fdi.model.events;

import java.util.ArrayList;

import es.ucm.fdi.model.simulation.TrafficSimulation;

public class FaultyVehicle extends Event {

	private ArrayList<String> vehicles;
	private int duration;
	
	public FaultyVehicle(int newTime, ArrayList<String> v, int dur){
		super(newTime);
		vehicles = v;
		duration = dur;
	}
	
	@Override
	public void execute(TrafficSimulation sim) {
		

	}

}
