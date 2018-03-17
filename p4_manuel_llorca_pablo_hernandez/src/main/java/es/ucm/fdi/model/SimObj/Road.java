package es.ucm.fdi.model.SimObj;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.simulation.SimulationException;

public class Road extends SimObject {
	protected final String REPORT_TITLE = "[road_report]";
	
	private int length;
	protected int speedLimit;
	private Junction fromJunction;
	private Junction toJunction;

	/**
	 * Lista de vehículos en la carretera que no están esperando
	 * en el cruce.
	 */
	protected ArrayList<Vehicle> vehiclesOnRoad;

	/**
	 * Lista temporal reutilizada en cada tick en la que se ordenan
	 * los vehículos que llegan al cruce por tiempo de llegada al
	 * cruce.
	 */
	private ArrayList<ArrivedVehicle> arrivalsToWaiting;

	/**
	 * Lista de vehículos en la carretera que están esperando en
	 * el cruce.
	 */
	private ArrayDeque<Vehicle> waiting;
	private boolean isGreen;
	
	/**
	 * Comparador según la localización de 2 vehículos en la carretera,
	 * para ordenar vehiclesOnRoad tras cada avance de los coches.
	 */
	private static class CompByLocation implements Comparator<Vehicle> {
		@Override
		public int compare(Vehicle v1, Vehicle v2) {
			int dist = v2.getLocation() - v1.getLocation();
			return dist; // ARREGLAR para (dist == 0)
		}

		// ROADEND - (v1, 80) < (v2, 78) < (v3, 50) < (v4, 20) - ROADBEGIN
	}

	/**
	 * Comparador según el tiempo de llegada al final de la carretera, para
	 * ordenar los arrivedVehicles según su tiempo de llegada.
	 */
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

	/**
	 * Clase interna que guarda cada vehículo con su tiempo de llegada
	 * al final de la carretera.
	 */
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
		
	
	public Road(String identifier, int len, int spLimit, Junction fromJ, Junction toJ) {
		super(identifier);
		length = len;
		speedLimit = spLimit;
		fromJunction = fromJ;
		toJunction = toJ;

		// Listas vacías
		vehiclesOnRoad = new ArrayList<>();
		arrivalsToWaiting = new ArrayList<>();
		waiting = new ArrayDeque<>();

		// Actualización de cruces afectados.
		getInOwnJunctions();	
		
		// Comprobación de semáforo
		if (isFirstIncoming()) {
			isGreen = true;
		}
		else {
			isGreen = false;
		}
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
		// * //
		// Se crea lista con los vehículos en la carretera en ese momento,
		// pues pueden salir durante su proceed y provocar un error en el foreach
		ArrayList<Vehicle> onRoad = new ArrayList<>();
		for (Vehicle v : vehiclesOnRoad) {
			onRoad.add(v);
		}

		// 1 //
		// Se modifica la velocidad a la que avanzarán los vehículos,
		// teniendo en cuenta el factor de reducción.
		vehicleSpeedModifier(onRoad);

		// 2 //
		// Los vehículos avanzan y se pueden adelantar.
		for (Vehicle v : onRoad) {
			v.proceed();
		}
		vehiclesOnRoad.sort(new CompByLocation());

		// 3 //
		// Los coches que llegan al final entran por orden en la cola de espera.
		pushArrivalsToWaiting();
	}

	/**
	 * Calcula la velocidad base de la Road: el mínimo entre el límite
	 * de velocidad y la velocidad que permite la congestión del tráfico en
	 * la Road.
	 */
	protected int getBaseSpeed() {
		// Cálculo de velocidadBase según la fórmula
		int congestionSpeed = ( speedLimit / Math.max(vehiclesOnRoad.size(), 1) ) + 1;

		return Math.min(speedLimit, congestionSpeed);
	}

	/**
	 * Modifica la velocidad que llevarán los vehículos en la
	 * carretera previo avance.
	 */
	protected void vehicleSpeedModifier(ArrayList<Vehicle> onRoad) {
		// Velocidad máxima a la que pueden avanzar los vehículos.
		int baseSpeed = getBaseSpeed();
		
		// Factor de reducción de velocidad en caso de obstáculos delante.
		int reductionFactor = 1;

		// Se modifica la velocidad a la que avanzarán los vehículos,
		// teniendo en cuenta el factor de reducción.
		for (Vehicle v : onRoad) {
			v.setSpeed(baseSpeed / reductionFactor);

			if (v.getBreakdownTime() > 0) {
				reductionFactor = 2;
			}
		}
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

		// Se vacía el array para el siguiente tick
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
	public void moveWaitingVehicles() throws SimulationException {
		// EXCEPCIÓN: si se llama con semáforo en rojo.
		if ( isGreen ) {
			// Saca al primer vehículo que está esperando.
			Vehicle moving = waiting.pollFirst();

			// Si hay algún vehículo y no está averiado.
			if (moving != null && moving.getBreakdownTime() == 0) {
				// Se mueve a la siguiente carretera.
				moving.moveToNextRoad();
			}
		}
		else {
			throw new SimulationException("Tried to advance waiting vehicle with red traffic lights in road with id: " + id);
		}	
	}

	/**
	 * Actualiza el estado de los vehículos averiados en la cola de espera.
	 */
	public void refreshWaiting() {
		for ( Vehicle v : waiting ) {
			if ( v.getBreakdownTime() > 0 ) {
				v.setBreakdownTime(-1);
			}
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
		
		// Borrado de última coma
		if (state.length() > 0) {
			state.deleteCharAt(state.length() - 1);
			// En caso contrario, queues es vacío y produciría
			// una OutOfBoubdsException.
		}

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
			if (waiting.size() > 0) {
				state.deleteCharAt(state.length() - 1);
			}
			state.append("]");
		}
		state.append(")");

		return state;
	}

	/**
	 * A partir de los datos de la carretera genera una IniSection
	 * @param simTime tiempo del simulador
	 * @return IniSection report de la carretera
	 */
	public IniSection generateIniSection(int simTime){
		String tag = REPORT_TITLE;
		//Creación de etiqueta (sin corchetes)
		tag = (String) tag.subSequence(1, tag.length() - 1);
		IniSection section = new IniSection(tag);
		section.setValue("id", id);
		section.setValue("time", simTime);
		section.setValue("state", getRoadState().toString());
		return section;
	}
	
	/**
	 * Actualiza sus cruces de salida y destino para que incluyan
	 * la carretera en sus listas de entrada y salida.
	 */
	private void getInOwnJunctions() {
		fromJunction.getExitRoads().add(this);
		toJunction.getIncomingRoads().add(this);
	}
	
	private boolean isFirstIncoming() {
		if ( this == toJunction.getIncomingRoads().get(0) ) {
			return true;
		}
		else {
			return false;
		}
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

	/**
	 * Devuelve la Junction desde la que empieza la
	 * carretera.
	 * @returns from Junction
	 */
	public Junction getFromJunction() {
		return fromJunction;
	}

	/**
	 * Devuelve la Junction desde la que empieza la
	 * carretera.
	 * @returns from Junction
	 */
	public Junction getToJunction() {
		return toJunction;
	}
	
	public boolean equals(Object obj){
		boolean same;
		same = super.equals(obj);
		if(same){
			Road other = (Road) obj;
			same = same && length == other.length;
			same = same && speedLimit == other.speedLimit;
			same = same && fromJunction.equals(other.fromJunction);
			same = same && toJunction.equals(other.toJunction);
		}
		return same;
	}
	
}
