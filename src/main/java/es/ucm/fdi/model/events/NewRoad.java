package es.ucm.fdi.model.events;

import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.SimObj.Road;
import es.ucm.fdi.model.simulation.AlreadyExistingSimObjException;
import es.ucm.fdi.model.simulation.NonExistingSimObjException;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public class NewRoad extends Event {
	
	protected String id;
	protected int length;
	protected int speedLimit;
	protected String fromJunctionID;
	protected String toJunctionID;	

	public NewRoad(int newTime, String ID, int lgth, int lim, String fromID, String toID) {
		super(newTime);
		id = ID;
		fromJunctionID = fromID;
		toJunctionID = toID;
		speedLimit = lim;
		length = lgth;
	}
	
	@Override
	public void execute(TrafficSimulation sim) throws AlreadyExistingSimObjException, NonExistingSimObjException {
		if ( ! sim.existsRoad(id) ) {
			try {
				sim.addRoad( newRoad(sim) );			
			}
			catch (NonExistingSimObjException e) {
				throw e;
			}
		}
		else {
			throw new AlreadyExistingSimObjException("Road with id: " + id + " already in simulation.");
		}

	}

	/**
	 * Devuelve una instancia de carretera genérica con los atributos
	 * del evento.
	 */
	protected Road newRoad(TrafficSimulation sim) throws NonExistingSimObjException {
		Junction fromJunction, toJunction;
		fromJunction = sim.getJunction(fromJunctionID);
		toJunction = sim.getJunction(toJunctionID);

		if ( fromJunction != null && toJunction != null ) {
			return new Road(id, length, speedLimit, fromJunction, toJunction);
		}
		else {
			throw new NonExistingSimObjException("One or both junctions from Road with id: " + id + " don't exist.");
		}

	}
	
	public boolean equals(Object obj){
		boolean same;
		same = super.equals(obj);
		if(same){
			NewRoad other = (NewRoad) obj;
			same = same && id == other.id;
			same = same && length == other.length;
			same = same && fromJunctionID.equals(other.fromJunctionID);
			same = same && toJunctionID.equals(other.toJunctionID);
		}
		return same;
	}
	
}
