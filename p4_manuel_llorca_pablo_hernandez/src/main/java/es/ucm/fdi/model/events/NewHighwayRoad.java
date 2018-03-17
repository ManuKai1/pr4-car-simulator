package es.ucm.fdi.model.events;

import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.SimObj.HighwayRoad;
import es.ucm.fdi.model.simulation.NonExistingSimObjException;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public class NewHighwayRoad extends NewRoad {
    
    private int numLanes;

    public NewHighwayRoad(int newTime, String ID, int lgth, int lim, String fromID, String toID, int lanes) {
        super(newTime, ID, lgth, lim, fromID, toID);
        numLanes = lanes;
    }

    /**
     * Devuelve una instancia de carretera (autopista) con los atributos
     * del evento.
     */
    @Override
    protected HighwayRoad newRoad(TrafficSimulation sim) throws NonExistingSimObjException {
        Junction fromJunction, toJunction;
        fromJunction = sim.getJunction(fromJunctionID);
        toJunction = sim.getJunction(toJunctionID);

        if (fromJunction != null && toJunction != null) {
            return new HighwayRoad(id, length, speedLimit, fromJunction, toJunction, numLanes);
        } else {
            throw new NonExistingSimObjException("One or both junctions from Road with id: " + id + " don't exist.");
        }
    }
}