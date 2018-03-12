package es.ucm.fdi.model.SimObj;

import es.ucm.fdi.model.simulation.RoadMap;

public class Junction extends SimObject {
	
	private final String REPORT_TITLE = "[junction_report]";

	//Entero que mediante la operación módulo representa el semáforo encendido. Empieza en 0.
	private int light;
	private RoadMap map;
	/*
	* Como atributo para no llamarla como parámetro en el
	* método proceed() [interface]. Y dado que Road y Vehicle
	* parecen no necesitar un RoadMap.
	*
	* Se inicializa en constructor.
	*/
	
	
	
	
	public Junction(String id) {
		this.id = id;
	}

	/**
	 * Método de AVANCE de Junction. En primer lugar, provoca el
	 * avance de los vehículos de sus carreteras salientes. En segundo
	 * lugar, provoca el paso de los vehículos de la carretera entrante
	 * con el semáforo en verde. Finalmente, se actualiza el semáforo
	 * circular.
	 */
	@Override
	public void proceed() {
		// 1 //
		// Los vehículos de las carreteras salientes avanzan.
		for (Road r : map.getExitRoadsOf(this)) {
			r.proceed();
		}

		// 2 //
		// Los vehículos esperando en las carreteras entrantes avanzan
		// si el semáforo se lo permite.		

		// Carretera con el semáforo en verde.
		Road greenRoad = map.getIncomingRoad(this, light);

		if (!greenRoad.noVehiclesWaiting()) {
			//Si el avance es posible, el vehículo se elimina de la cola.
			//Visto en el enunciado; ¿por qué no iba a ser posible el avance?
			// TRY ... CATCH ...
			greenRoad.moveWaitingVehicles();
		}

		// 3 //
		// Se actualiza el semáforo del cruce (y el indicador resp. de la carretera).
		greenRoad.setLight(false);
		lightAdvance();
	}
	
	/**
	 * Actualiza el semáforo circular de una Junction, poniendo el
	 * semáforo de la Road indicada a 'true'.
	 */
	private void lightAdvance() {
		// Número de carreteras entrantes en el cruce.
		// COMMENT : Quizá mejor en el constructor, pues no tendría
		// sentido que cambiase el tamaño durante la simulación.
		int numIncomingRoads = map.getIncomingSizeIn(this);
		
		// Avanza en 1 el semáforo circular.
		light = (light + 1) % numIncomingRoads;

		// El semáforo de la carretera se pone verde.
		map.getIncomingRoad(this, light).setLight(true);
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
		for ( Road incR : map.getIncomingRoadsOf(this) ) {
			report.append(incR.getWaitingState());
			report.append(",");
		}

		//Borrado de última coma
		report.deleteCharAt(report.length() - 1);

		return report.toString();
	}	
}
