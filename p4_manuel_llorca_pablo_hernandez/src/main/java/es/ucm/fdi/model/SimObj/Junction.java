package es.ucm.fdi.model.SimObj;

import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
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
		// Todos los semáforos en rojo al principio.
		light = -1;
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
		if (light != -1) {
			Road greenRoad = incomingRoads.get(light);

			if ( ! greenRoad.noVehiclesWaiting() ) {
				// El vehículo cruza si no está averiado.
				try {
					greenRoad.moveWaitingVehicles();
				}
				catch (SimulationException e) {
					System.err.println( e.getMessage() );
				}
			}

			// 2 //
			// Se actualiza el indicador resp. de la carretera.
			greenRoad.setLight(false);
			
			// 3 //
			// Se reduce el tiempo de avería de los vehículos en la cola de espera.
			greenRoad.refreshWaiting();
		}
		
		// 4 //
		// Se actualiza el semáforo del cruce.
		lightAdvance();		
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

		// Borrado de última coma
		if (report.length() > 0) {
			report.deleteCharAt(report.length() - 1);
		}
		

		return report.toString();
	}
	
	/**
	 * A partir de los datos del cruce genera una IniSection
	 * @param simTime tiempo del simulador
	 * @return IniSection report del cruce
	 */
	public IniSection generateIniSection(int simTime){
		String tag = REPORT_TITLE;
		//Creación de etiqueta (sin corchetes)
		tag = (String) tag.subSequence(1, tag.length() - 1);
		IniSection section = new IniSection(tag);
		section.setValue("id", id);
		section.setValue("time", simTime);
		StringBuilder queues = new StringBuilder();
		//Generación del string de queues
		for ( Road incR : incomingRoads ) {
			queues.append(incR.getWaitingState());
			queues.append(",");
		}
		
		// Borrado de última coma
		if (queues.length() > 0) {
			queues.deleteCharAt(queues.length() - 1);
			// En caso contrario, queues es vacío y produciría
			// una OutOfBoundsException.
		}
		
		section.setValue("queues", queues.toString());
		return section;
	}
	
	/**
	 * Devuelve el ArrayList de carreteras entrantes
	 * @return arrayList de carreteras entrantes.
	 */
	public ArrayList<Road> getIncomingRoads() {
		return incomingRoads;
	}
	
	/**
	 * Comprueba si el cruce tiene carreteras entrantes.
	 */
	public boolean hasIncomingRoads() {
		return (incomingRoads.size() > 0);
	}

	/**
	 * Devuelve el ArrayList de carreteras salientes
	 * @return arrayList de carreteras salientes.
	 */
	public ArrayList<Road> getExitRoads() {
		return exitRoads;
	}
	
	public boolean equals(Object obj){
		boolean same;
		same = super.equals(obj);
		if(same){
			Junction other = (Junction) obj;
			same = same && light == other.light;
			same = same && incomingRoads.equals(other.incomingRoads);
			same = same && exitRoads.equals(other.exitRoads);
		}
		return same;
	}
	
}
