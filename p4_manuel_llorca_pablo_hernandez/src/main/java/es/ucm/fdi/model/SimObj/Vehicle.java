package es.ucm.fdi.model.SimObj;

import java.util.ArrayList;
import java.util.Iterator;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.simulation.SimulationException;

public class Vehicle extends SimObject {
	protected final String REPORT_TITLE = "[vehicle_report]";
	
	protected ArrayList<Junction> trip;
	protected int lastTripPos;
	protected int maxSpeed;
	protected int kilometrage;
	protected int breakdownTime;

	protected boolean hasArrived;
	protected boolean isWaiting;

	protected Road road;
	protected int location;
	protected int actualSpeed;	

	public Vehicle(String identifier, ArrayList<Junction> trp, int max) {
		super(identifier);
		trip = trp;
		maxSpeed = max;

		// Valores iniciales.
		lastTripPos = 0;
		kilometrage = 0;
		breakdownTime = 0;

		hasArrived = false;
		isWaiting = false;

		// Se mete en la primera carretera.
		try {
			road = getRoadBetween( trip.get(lastTripPos), trip.get(lastTripPos + 1) );
			road.pushVehicle(this);
		}
		catch (SimulationException e) {
			System.err.println( e.getMessage() );
		}

		location = 0;
		actualSpeed = 0; // Irrelevante.
	}
	
	/**
	 * Método de AVANCE de Vehicle. En primer lugar, comprueba si el vehículo
	 * está averiado. Si lo está, se reduce su tiempo de avería y no avanza.
	 * Si no lo está, se comprueba si el vehículo llegaría al final de la
	 * carretera en este turno. Si es así, se le hace esperar en el Junction.
	 * Si no, se modifica su locacalización con su velocidad.
	 */
	@Override
	public void proceed() {
		// Comprobamos primero si el vehículo está averiado o no
		if (breakdownTime > 0) {
			breakdownTime--;
		}
		else {
			// Comprobamos si el vehículo llega al cruce.
			if (location + actualSpeed >= road.getLength()) {
				kilometrage += (road.getLength() - location);
				waitInJunction();
			}
			else {
				location += actualSpeed;
				kilometrage += actualSpeed;
			}
		}		
	}

	/**
	 * Saca al vehículo de road.vehiclesOnRoad y lo introduce en 
	 * road.arrivalsToWaiting, a la espera de ser introducido en
	 * road.waiting una vez se hayan movido todos los Vehicle.
	 */
	public void waitInJunction() {
		// Saca al vehículo de la zona de circulación de la Road
		road.popVehicle(this);
		
		// Cálculo del tiempo de llegada.
		float arrivalTime = ( actualSpeed / (road.getLength() - location) );
		// Se mete el Vehicle en la lista de llegados a la cola de espera.
		// Será introducido en road.waiting una vez que todos hayan llegado.
		road.arriveToWaiting(this, arrivalTime);	
		
		// Localización = longitud de Road
		location = road.getLength();
		isWaiting = true;
		actualSpeed = 0;
	}
	
	/**
	* Mueve el vehículo a la carretera siguiente en su itinerario.
	* Anteriormente, el vehículo debe haber salido de la carretera en
	* la que estaba esperando.
	*/
	public void moveToNextRoad() {
		int waitingPos = lastTripPos + 1;
		int nextWaitingPos = waitingPos + 1;

		if ( nextWaitingPos == trip.size() ) {
			// Última vez. El cruce donde se espera es el destino final.
			hasArrived = true;
		}				 
		else {
			// Cambio normal de una Road a otra.
			try {
				road = getRoadBetween(trip.get(waitingPos), trip.get(nextWaitingPos));
				road.pushVehicle(this);

				location = 0;
			} catch (SimulationException e) {
				System.err.println( e.getMessage() );
			}			
		}

		lastTripPos++;
		isWaiting = false;
	}

	/**
	 * Método privado de Vehicle que devuelve la carretera entre dos cruces. Para ser utilizada
	 * con dos cruces consecutivos de la ruta del vehículo en cuestión.
	 * @param fromJunction cruce de origen
	 * @param toJunction cruce de destino
	 * @throws SimulationException if road between Junctions not found
	 * @return carretera entre los dos cruces
	 */
	private Road getRoadBetween(Junction fromJunction, Junction toJunction) throws SimulationException {
		// Carreteras de salida y entrada.
		ArrayList<Road> fromRoads = fromJunction.getExitRoads();
		ArrayList<Road> toRoads = toJunction.getIncomingRoads();
		// Carretera buscada.
		Road searched = null;

		// Se recorren las carreteras de salida.
		boolean found = false;
		Iterator<Road> fromIt = fromRoads.iterator();
		while (fromIt.hasNext() && !found) {
			Road fromR = fromIt.next();

			// Se recorren las carreteras de entrada.
			Iterator<Road> toIt = toRoads.iterator();
			while (toIt.hasNext() && !found) {
				Road toR = toIt.next();
				if (toR == fromR) {
					found = true;
					searched = fromR;
				}
			}
		}

		if (found) {
			return searched;
		} else {
			throw new SimulationException(
				"Road not fot found on route of vehicle with id: " + id + 
				" between junctions with id: " + fromJunction.getID() + ", " 
				+ toJunction.getID());
		}
	}

	/**
	 * Informe de el Vehicle en cuestión, mostrando: id, tiempo de simulación,
	 * velocidad actual, kilometraje, tiempo de avería, localización, llegada a
	 * destino
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
		// Velocidad actual
		report.append("speed = " + actualSpeed + '\n');
		// Kilometraje
		report.append("kilometrage = " + kilometrage + '\n');
		// Tiempo de avería
		report.append("faulty = " + breakdownTime + '\n');
		// Localización
		report.append("location = ");
		report.append( hasArrived ? "arrived" : "(" + road.getID() + "," + location + ")");

		return report.toString();
	}
	
	/**
	 * A partir de los datos del vehículo genera una IniSection
	 * @param simTime tiempo del simulador
	 * @return IniSection report del vehículo
	 */
	public IniSection generateIniSection(int simTime){
		String tag = REPORT_TITLE;
		//Creación de etiqueta (sin corchetes)
		tag = (String) tag.subSequence(1, tag.length() - 1);
		IniSection section = new IniSection(tag);
		section.setValue("id", id);
		section.setValue("time", simTime);
		section.setValue("speed", actualSpeed);
		section.setValue("kilometrage", kilometrage);
		section.setValue("faulty", breakdownTime);
		section.setValue("location", hasArrived ? "arrived" : "(" + road.getID() + "," + location + ")");
		return section;
	}
	
	
	/**
	 * Modifica el tiempo de avería.
	 * @param addedBreakdownTime tiempo de avería a sumar
	 */
	public void setBreakdownTime(int addedBreakdownTime)  {
		breakdownTime += addedBreakdownTime;
	}	
	
	/**
	 * Modifica la velocidad de la carretera como el mínimo entre
	 * la velocidad que permite la Road y la velocidad máxima de Vehicle.
	 * @param roadSpeed velocidad permitida por la carretera
	 */
	public void setSpeed(int roadSpeed) {
		if (breakdownTime == 0 ) {
			actualSpeed = Math.min(roadSpeed, maxSpeed);
		}
		else {
			actualSpeed = 0;
		}
	}	
	
	/**
	 * @returns tiempo de avería
	 */
	public int getBreakdownTime(){
		return breakdownTime;
	}
	
	/**
	 * @returns if Vehicle is waiting
	 */
	public boolean getIsWaiting(){
		return isWaiting;
	}
	
	/**
	 * @returns the vehicle's Road
	 */
	public Road getRoad(){
		return road;
	}

	/**
	 * @returns the vehicle's location in the road
	 */
	public int getLocation() {
		return location;
	}
	
	/**
	 * @returns true if vehicle is faulty
	 */
	public boolean isFaulty(){
		return breakdownTime > 0;
	}
	
	public boolean equals(Object obj){
		boolean same;
		same = super.equals(obj);
		if(same){
			Vehicle other = (Vehicle) obj;
			same = same && trip.equals(other.trip);
			same = same && lastTripPos == other.lastTripPos;
			same = same && maxSpeed == other.maxSpeed;
			same = same && kilometrage == other.kilometrage;
			same = same && breakdownTime == other.breakdownTime;
			same = same && hasArrived == other.hasArrived;
			same = same && isWaiting == other.isWaiting;
			same = same && road.equals(other.road);
			same = same && location == other.location;
			same = same && actualSpeed == other.actualSpeed;	
		}
		return same;
	}
	
}


