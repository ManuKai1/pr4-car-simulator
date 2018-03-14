package es.ucm.fdi.model.simulation;

import java.util.ArrayList;

import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.SimObj.Road;
import es.ucm.fdi.model.SimObj.Vehicle;

public class RoadMap {
    
    /*
     * Character representa los
     * distintos objetos de simulación ('J', 'R', 'V'), simObject para
     * añadir listas de objetos creados.
     */
    
    // MAPA
    private ArrayList<Junction> junctionObjects;
    private ArrayList<Road> roadObjects;
    private ArrayList<Vehicle> vehicleObjects;


    public RoadMap() {
        junctionObjects = new ArrayList<>();
        roadObjects = new ArrayList<>();
        vehicleObjects = new ArrayList<>();
    }

    // MÉTODOS

    /**
     * Devuelve la lista de carreteras.
     */
    public ArrayList<Road> getRoads() {
        return roadObjects;
    }

    /**
     * Devuelve la lista de cruces.
     */
    public ArrayList<Junction> getJunctions() {
        return junctionObjects;
    }
    
    /**
     * Devuelve la lista de vehículos.
     */
    public ArrayList<Vehicle> getVehicles() {
        return vehicleObjects;
    }

    /**
     * Añade un cruce a la lista de cruces.
     */
    public void addJunction(Junction newJunction) {
        junctionObjects.add(newJunction);
    }

    /**
    * Añade una carretera a la lista de carreteras.
    */
    public void addRoad(Road newRoad) {
        roadObjects.add(newRoad);
    }
    
    /**
     * Añade un vehículo a la lista de vehículos.
     */
    public void addVehicle(Vehicle newVehicle) {
        vehicleObjects.add(newVehicle);
    }

    public boolean existsJunctionID(String id) {
        // O(n)
        for (Junction j : junctionObjects) {
            if (j.getID().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public boolean existsRoadID(String id) {
        // O(n)
        for ( Road r : roadObjects ) {
            if ( r.getID().equals(id) ) {
                return true;
            }
        }

        return false;
    }

    public boolean existsVehicleID(String id) {
        // O(n)
        for ( Vehicle v : vehicleObjects ) {
            if ( v.getID().equals(id) ) {
                return true;
            }
        }

        return false;
    }

    public Vehicle getVehicleWithID(String id) {
        // O(n)
        for ( Vehicle v : vehicleObjects ) {
            if ( v.getID().equals(id) ) {
                return v;
            }
        }

        return null;
    }


    public Junction getJunctionWithID(String id) {
        // O(n)
        for ( Junction j : junctionObjects ) {
            if ( j.getID().equals(id) ) {
                return j;
            }
        }

        return null;
    }



    
}