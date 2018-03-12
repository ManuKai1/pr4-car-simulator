package es.ucm.fdi.model.SimObj;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Map;

import es.ucm.fdi.model.events.NewRoad;

public class Road extends SimObject {
	final private String REPORT_TITLE = "[road_report]";
	
	private int length;
	private int speedLimit;

	private ArrayList<Vehicle> vehiclesOnRoad;

	private ArrayList<ArrivedVehicle> arrivalsToWaiting;
	// COMMENT: Un coche puede llegar antes por velocidad,
	// aunque su proceed() se haga por el orden de vehiclesOnRoad.
	// Float es la razón actualSpeed / distanceToEnd.
	// VER: arriveToWaiting()

	private ArrayDeque<Vehicle> waiting;
	private boolean isGreen;
	
	// Para recolocar los coches tras el avance.
	private static class CompByLocation implements Comparator<Vehicle> {
		@Override
		public int compare(Vehicle v1, Vehicle v2) {
			int dist = v2.getLocation() - v1.getLocation();
			return dist; // ARREGLAR para (dist == 0)
		}

		// ROADEND - (v1, 80) < (v2, 78) < (v3, 50) < (v4, 20) - ROADBEGIN
	}

	// Para meter a los coches a la cola de espera.
	private static class CompArrivedVehicles implements Comparator<ArrivedVehicle> {
		@Override
		public int compare(ArrivedVehicle av1, ArrivedVehicle av2) {
			// Si av1.time < av2.time -> av1 < av2
			float diff = av1.getTime() - av2.getTime();
			
			int intDiff;
			if ( diff < 0 ) intDiff = -1;
			else intDiff = 1;
			
			return intDiff;
		}

		// ROADEND - (v1, 0.1s) < (v2, 0.5s) < (v3, 2s) < (v4, 3s) - ROADBEGIN
	}

	// Para los coches que llegan al cruce.
	private class ArrivedVehicle {
		private Vehicle arrived;
		private float time;

		public ArrivedVehicle(Vehicle arr, float t) {
			arrived = arr;
			time = t;
		}

		public Vehicle getArrived() {
			return arrived;
		}
		
		public float getTime() {
			return time;
		}
	}
		
	
	public Road(NewRoad builder) { //¿?//
		
	}

	/**
	 * Método de AVANCE de Road. En primer lugar, modifica la velocidad
	 * que llevarán los coches durante el avance, teniendo en cuenta factores
	 * de la Road. En segundo lugar, provoca el avance de los vehículos en la
	 * Road y los reordena si ha habido adelantamientos. Finalmente, introduce
	 * a los vehículos que han llegado al final de la Road en la cola de espera.
	 */
	@Override
	public void proceed() {
		// Velocidad máxima a la que pueden avanzar los vehículos.
		int baseSpeed = getBaseSpeed();
		// Factor de reducción de velocidad en caso de obstáculos delante.
		int reductionFactor = 1;

		// 1 //
		// Se modifica la velocidad a la que avanzarán los vehículos,
		// teniendo en cuenta el factor de reducción.
		for (Vehicle v : vehiclesOnRoad) {
			v.setSpeed(baseSpeed / reductionFactor);

			if (v.getBreakdownTime() > 0) {
				reductionFactor = 2;
			}
		}

		// 2 //
		// Los vehículos avanzan y se pueden adelantar.
		for (Vehicle v : vehiclesOnRoad) {
			v.proceed();
		}
		vehiclesOnRoad.sort(new CompByLocation());

		// 3 //
		// Los coches que llegan al final entran por orden la col de espera.
		pushArrivalsToWaiting();
	}

	/**
	 * Calcula la velocidad base de la Road: el mínimo entre el límite
	 * de velocidad y la velocidad que permite la congestión del tráfico en
	 * la Road.
	 */
	public int getBaseSpeed() {
		// Cálculo de velocidadBase según la fórmula
		int congestionSpeed = ( speedLimit / Math.max(vehiclesOnRoad.size(), 1) ) + 1;

		return Math.min(speedLimit, congestionSpeed);
	}

	/**
	 * Inserta los vehículos que han llegado al final de la Road en la 
	 * cola de espera, ordenados por tiempo de llegada.
	 */
	public void pushArrivalsToWaiting() {
		// Se hace cuando han avanzado todos los coches.
		arrivalsToWaiting.sort(new CompArrivedVehicles());

		// Se insertan ordenados en la cola de espera.
		for (ArrivedVehicle av : arrivalsToWaiting) {
			waiting.addLast(av.getArrived());
		}

		// Se vacía el array para el siguiente tic
		arrivalsToWaiting.clear();
	}

	/**
	 * Guarda un vehículo y su tiempo de llegada en la lista de coches
	 * que van a entrar en la cola de espera.
	 */
	public void arriveToWaiting(Vehicle toWait, float arrivalTime) {
		// Se guarda en el Map su información de llegada.
		arrivalsToWaiting.add(new ArrivedVehicle(toWait, arrivalTime));
	}	

	/**
	 * Mueve los Vehicle a la espera en un Junction a sus respectivas
	 * Road de salida.
	 */
	public void moveWaitingVehicles() {
		// EXCEPCIÓN: si se llama con semáforo en rojo.
		
		// Saca al primer vehículo que está esperando.
		Vehicle moving = waiting.pollFirst();

		if (moving != null) {
			// Se mueve a la siguiente carretera.
			moving.moveToNextRoad();
		} else {
			// EXCEPCION: No hay ningún vehículo esperando.
			// throw ...
		}
	}

	/**
	 * Informe de la Road en cuestión, mostrando: id,
	 * tiempo de simulación,
	 * @param simTime tiempo de simulación
	 * @returns well-formatted String representing a Road report
	 */
	@Override
	public String getReport(int simTime) {
		StringBuilder report = new StringBuilder();
		// TITLE
		report.append(REPORT_TITLE + '\n');
		// ID
		report.append("id = " + id + '\n');
		// SimTime
		report.append("time = " + simTime + '\n');
		// Road State
		report.append("state = ");
		report.append(getRoadState());

		return report.toString();
	}

	/**
	 * Devuelve un StringBuilder con el estado de la Road.
	 * Ejemplo:
	 * (v1, 80), (v3, 80), (v2, 76), (v5, 33)
	 * @returns StringBuilder with state of road
	 */
	public StringBuilder getRoadState() {
		StringBuilder state = new StringBuilder();

		// Primero los vehículos en la cola de espera.
		for (Vehicle v : waiting) {
			// ID
			state.append("(" + v.getID());
			// Location
			state.append("," + v.getLocation());

			state.append("),");
		}

		// Después los vehículos en la carretera.
		for (Vehicle v : vehiclesOnRoad) {
			// ID
			state.append("(" + v.getID());
			// Location
			state.append("," + v.getLocation());

			state.append("),");
		}

		// Se quita la última coma.
		state.deleteCharAt(state.length() - 1);

		return state;
	}

	/**
	 * Devuelve un StringBuilder con el estado de la cola de
	 * espera de la Road. 
	 * Ejemplo:
	 * (r2,green,[v3,v2,v5])
	 * @returns StringBuilder with state of waiting list
	 */
	public StringBuilder getWaitingState() {
		StringBuilder state = new StringBuilder();
		// ID
		state.append("(" + getID() + ",");
		// Semáforo
		state.append(isGreen ? "green" : "red");
		// Cola de espera
		state.append(",[");
		if (waiting.isEmpty()) {
			state.append("]");
		} else {
			for (Vehicle v : waiting) {
				state.append(v.getID() + ",");
			}
			state.setCharAt(state.length(), ']');
		}
		state.append(")");

		return state;
	}

	/**
	 * Mete un vehículo al final de vehiclesOnRoad
	 */
	public void pushVehicle(Vehicle v) {
		vehiclesOnRoad.add(v);
	}

	/**
	 * Saca un vehículo de vehiclesOnRoad
	 * @param v vehículo a quitar
	 * @throws NoSuchElementException if v is not on vehiclesOnRoad
	 */
	public void popVehicle(Vehicle v) throws NoSuchElementException {
		if (!vehiclesOnRoad.remove(v)) {
			throw new NoSuchElementException("Vehicle to pop not found.");
		}
	}	
	
	/**
	 * @returns length of Road
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @returns if waiting cue is empty
	 */
	public boolean noVehiclesWaiting() {
		return waiting.isEmpty();
	}

	/**
	 * Método de activación de semáforo
	 * @param green nuevo estado del semáforo
	 */
	public void setLight(boolean green) {
		isGreen = green;
	}	
}
