package es.ucm.fdi.model.events;

import es.ucm.fdi.model.SimObj.DirtRoad;
import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.simulation.NonExistingSimObjException;
import es.ucm.fdi.model.simulation.TrafficSimulation;

public class NewDirtRoad extends NewRoad {

    public NewDirtRoad(int newTime, String ID, int lgth, int lim, String fromID, String toID) {
        super(newTime, ID, lgth, lim, fromID, toID);
    }

    /**
     * Devuelve una instancia de carretera (autopista) con los atributos
     * del evento.
     */
    @Override
    protected DirtRoad newRoad(TrafficSimulation sim) throws NonExistingSimObjException {
        Junction fromJunction, toJunction;
        fromJunction = sim.getJunction(fromJunctionID);
        toJunction = sim.getJunction(toJunctionID);

        if (fromJunction != null && toJunction != null) {
            return new DirtRoad(id, length, speedLimit, fromJunction, toJunction);
        } else {
            throw new NonExistingSimObjException("One or both junctions from Road with id: " + id + " don't exist.");
        }
    }
}