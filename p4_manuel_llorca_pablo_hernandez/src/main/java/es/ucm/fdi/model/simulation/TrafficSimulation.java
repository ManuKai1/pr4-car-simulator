package es.ucm.fdi.model.simulation;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.SimObj.Road;
import es.ucm.fdi.model.SimObj.Vehicle;
import es.ucm.fdi.model.events.Event;
import es.ucm.fdi.util.MultiTreeMap;

public class TrafficSimulation {

	/**
	 * Mapa de eventos donde: Integer representa el tiempo de
	 * ejecución de un evento, Event para añadir listas de eventos
	 * que se ejecutan en ese tiempo.
	 */
	private MultiTreeMap<Integer, Event> events;

	/**
	 * Mapa de simulación que relaciona Junction con sus carreteras entrantes
	 * y salientes.
	 */
	RoadMap roadMap;

	/**
	 * Tiempo actual de la simulación.
	 */
	private int time;

	public TrafficSimulation() {
		// Listas vacías. 
		events = new MultiTreeMap<>();
		roadMap = new RoadMap();
		// Tiempo inicial a 0
		time = 0;
	}
	
	/**
	 * Añade un evento al mapa de eventos de la simulación, comprobando
	 * que el tiempo del evento sea mayor que el de la simulación.
	 * @param e evento a añadir
	 * @throws SimulationException if event time lower thar sim time
	 */
	public void pushEvent(Event e) throws SimulationException {
		// Comprueba el tiempo.
		if( e.getTime() < time ) {
			throw new SimulationException("Event time is lower than current time.");
		}

		// Añade el evento al mapa.
		events.putValue(e.getTime(), e);
	}
	
	/**
	 * Simula un número determinado de ticks y guarda el fichero de salida
	 * de esta ejecución.
	 * @param steps número de pasos a ejecutar
	 * @param file fichero de salida
	 */
	public void execute(int steps, OutputStream file) {
		int timeLimit = time + steps - 1;
		while (time <= timeLimit) {
			// Se ejecutan los eventos correspondientes a ese tiempo.
			if ( events.get(time) != null ) {
				for ( Event event : events.get(time) ) {
					try {
						event.execute(this);
					}
					catch (SimulationException e1) {
						System.err.println( e1.getMessage() );
					}				
				}
			}			

			// Para cada carretera, los coches que no están esperando avanzan.
			for ( Road road : roadMap.getRoads() ) {
				road.proceed();
			}

			// Para cada cruce, avanzan los vehículos a la espera que puedan y se actualiza 
			// el semáforo y los tiempos de avería de los vehículos a la espera.
			for ( Junction junction : roadMap.getJunctions() ) {
				if ( junction.hasIncomingRoads() ) {
					junction.proceed();
				}				
			}

			// Se avanza un tick.
			time++;
			// Escribir un informe en OutputStream
			// en caso de que no sea nulo
			if(file != null) {
				//Creación de ini
				Ini iniFile = new Ini();
				//Junctions:
				for(Junction junction : roadMap.getJunctions()){
					iniFile.addsection(junction.generateIniSection(time));
				}
				//Roads:
				for(Road road : roadMap.getRoads()){
					iniFile.addsection(road.generateIniSection(time));
				}
				//Vehicles:
				for(Vehicle vehicle : roadMap.getVehicles()){
					iniFile.addsection(vehicle.generateIniSection(time));
				}
				
				// Guardado en el outputStream
				try{
					iniFile.store(file);
				}
				catch(IOException e){
					System.err.println("Error when saving file on time " + time + ":" + e.getMessage());
				}
			}
		}
	}

	/**
	 * Añade tiempo de avería a los vehículos con los ID de la lista.
	 * Además comprueba que existan los vehículos referenciados por esos IDs.
	 * @param vehiclesID lista de IDs de los vehículos a averiar
	 * @param breakDuration duración del tiempo de avería a añadir
	 */
	public void makeFaulty(ArrayList<String> vehiclesID, int breakDuration) throws NonExistingSimObjException {
		// O(n^2)
		for ( String id : vehiclesID ) {
			Vehicle toBreak = getVehicle(id);
			if ( toBreak != null ) {
				toBreak.setBreakdownTime(breakDuration);
			}
			else {
				throw new NonExistingSimObjException("Vehicle with id: " + id + " to make faulty not found.");
			}
		}
	}

	public void addVehicle(Vehicle newVehicle) {
		// Se guarda en el inventario de objetos de simulación.
		roadMap.addVehicle(newVehicle);
	}

	public void addRoad(Road newRoad) {
		// Se mete en el RoadMap.
		roadMap.addRoad(newRoad);
	}

	public void addJunction(Junction newJunction) {
		// Se mete en el RoadMap
		roadMap.addJunction(newJunction);
	}

	/**
	 * Busca en el mapa si hay un vehículo con el mismo ID.
	 * @param id id a buscar
	 * @returns si hay un Vehicle con el id dado
	 */
	public boolean existsVehicle(String id) {
		return roadMap.existsVehicleID(id);
	}

	/**
	 * Busca en el mapa si hay un cruce con el mismo ID.
	 * @param id id a buscar
	 * @returns si hay un Junction con el id dado
	 */
	public boolean existsJunction(String id) {
		return roadMap.existsJunctionID(id);
	}

	/**
	 * Busca en el mapa si hay una carretera con el mismo ID.
	 * @param id id a buscar
	 * @returns si hay una Road con el id dado
	 */
	public boolean existsRoad(String id) {
		return roadMap.existsRoadID(id);
	}

	/**
	 * Busca en el mapa un vehículo con el mismo ID. 
	 * Devuelve ese Vehicle si lo encuentra o null en caso contrario.
	 * @param id id a buscar
	 * @returns Vehicle con ese id si existe; null en caso contrario
	 */
	private Vehicle getVehicle(String id) {
		return roadMap.getVehicleWithID(id);
	}

	/**
	 * Busca en el mapa un cruce con el mismo ID. 
	 * Devuelve ese Junction si lo encuentra o null en caso contrario.
	 * @param id id a buscar
	 * @returns Junction con ese id si existe; null en caso contrario
	 */
	public Junction getJunction(String id) {
		return roadMap.getJunctionWithID(id);
	}	
}
