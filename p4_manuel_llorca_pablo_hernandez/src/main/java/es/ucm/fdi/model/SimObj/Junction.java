package es.ucm.fdi.model.SimObj;

import java.util.ArrayList;

import es.ucm.fdi.model.simulation.SimulationException;

public class Junction extends SimObject {
	
	private final String REPORT_TITLE = "[junction_report]";

	/**
	 * Lista de carreteras entrantes en el cruce.
	 */
	private ArrayList<Road> incomingRoads;

	/**
	 * Lista de carreteras salientes en el cruce.
	 */
	private ArrayList<Road> exitRoads;

	/**
	 * Entero que mediante la operación módulo representa el semáforo encendido. Empieza en 0.
	 */
	private int light;	
	
	public Junction(String identifier) {
		super(identifier);
		// Listas vacías.
		incomingRoads = new ArrayList<>();
		exitRoads = new ArrayList<>();
		// Semáforo en verde para la primera carretera entrante.
		light = 0;
		incomingRoads.get(light).setLight(true);
	}

	/**
	 * Método de AVANCE de Junction. Provoca el paso de los vehículos 
	 * de la carretera entrante con el semáforo en verde. Finalmente, 
	 * se actualiza el semáforo circular.
	 */
	@Override
	public void proceed() {
		// 1 //
		// El primer vehículo esperando en la carretera con el semáforo
		// abierto avanza si no está averiado.

		// Carretera con el semáforo en verde.
		Road greenRoad = incomingRoads.get(light);

		if ( ! greenRoad.noVehiclesWaiting() ) {
			// El vehículo cruza si no está averiado.
			try {
				greenRoad.moveWaitingVehicles();
			}
			catch (SimulationException e) {
				// System.err.println( e.getMessage() );
			}
		}

		// 2 //
		// Se actualiza el semáforo del cruce (y el indicador resp. de la carretera).
		greenRoad.setLight(false);
		lightAdvance();

		// 3 //
		// Se reduce el tiempo de avería de los vehículos en la cola de espera.
		greenRoad.refreshWaiting();
	}
	
	/**
	 * Actualiza el semáforo circular de una Junction.
	 */
	private void lightAdvance() {
		// Número de carreteras entrantes en el cruce.
		int numIncomingRoads = incomingRoads.size();
		
		// Avanza en 1 el semáforo circular.
		light = (light + 1) % numIncomingRoads;

		// El semáforo de la carretera se pone verde.
		incomingRoads.get(light).setLight(true);
	}

	/**
	 * Informe de la Junction en cuestión, mostrando: id,
	 * tiempo de simulación, colas de espera de sus carreteras entrantes.
	 * @param simTime tiempo de la simulación 
	 */
	@Override
	public String getReport(int simTime) {
		StringBuilder report = new StringBuilder();
		// TITLE
		report.append(REPORT_TITLE + '\n');
		// ID
		report.append("id = " + id);
		// SimTime
		report.append("time = " + simTime);
		// Colas de espera
		report.append("queues = ");
		for ( Road incR : incomingRoads ) {
			report.append(incR.getWaitingState());
			report.append(",");
		}

		//Borrado de última coma
		report.deleteCharAt(report.length() - 1);

		return report.toString();
	}
	
	/**
	 * Devuelve el ArrayList de carreteras entrantes
	 * @return arrayList de carreteras entrantes.
	 */
	public ArrayList<Road> getIncomingRoads() {
		return incomingRoads;
	}

	/**
	 * Devuelve el ArrayList de carreteras salientes
	 * @return arrayList de carreteras salientes.
	 */
	public ArrayList<Road> getExitRoads() {
		return exitRoads;
	}
}
