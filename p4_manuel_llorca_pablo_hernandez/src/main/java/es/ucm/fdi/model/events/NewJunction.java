package es.ucm.fdi.model.events;

import es.ucm.fdi.ini.IniError;
import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.simulation.AlreadyExistingSimObjException;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public class NewJunction extends Event{

	private String id;
	
	public NewJunction(int newTime, String ID) throws IniError{
		super(newTime);
		id = ID;	
	}
	
	@Override
	public void execute(TrafficSimulation sim) throws AlreadyExistingSimObjException {
		if ( ! sim.existsJunction(id) ) {
			sim.addJunction( newJunction() );
		} 
		else {
			throw new AlreadyExistingSimObjException("Junction with id:" + id + " already in simulation.");
		}
	}
	
	private Junction newJunction() {
		return new Junction(id);
	}

	public boolean equals(Object obj){
		boolean same;
		same = super.equals(obj);
		if(same){
			NewJunction other = (NewJunction) obj;
			same = same && id == other.id;
		}
		return same;
	}
	
}
