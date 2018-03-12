package es.ucm.fdi.model.SimObj;

import java.util.ArrayList;

import es.ucm.fdi.model.simulation.RoadMap;

public class Vehicle extends SimObject {
	private final String REPORT_TITLE = "[vehicle_report]";
	
	private RoadMap map;
	private ArrayList<Junction> trip;
	private int lastTripPos;
	private int maxSpeed;
	private int kilometrage;
	private int breakdownTime;

	private boolean hasArrived;
	private boolean isWaiting;

	private Road road;
	private int location;
	private int actualSpeed;	

	public Vehicle() {
		// setTrip();

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
		// Localización = longitud de Road
		location = road.getLength();

		// Cálculo del tiempo de llegada.
		float arrivalTime = ( actualSpeed / (road.getLength() - location) );
		// Se mete el Vehicle en la lista de llegados a la cola de espera.
		// Será introducido en road.waiting una vez que todos hayan llegado.
		road.arriveToWaiting(this, arrivalTime);	

		isWaiting = true;
		actualSpeed = 0;
	}
	
	/**
		 * Mueve el vehículo a la carretera siguiente en su itinerario.
		 * Anteriormente, el vehículo debe haber salido de la carretera en
		 * la que estaba esperando.
		 */
	public void moveToNextRoad() {
		//Función de búsqueda de carreteras que van a nuestro siguiente cruce.
		//Elegimos la única que coincide.
		int waitingPos = lastTripPos + 1;
		int nextWaitingPos = waitingPos + 1;

		// Primera vez. El cruce desde el que se ha entrado a la Road
		// es el primero
		if (lastTripPos == 0) {
			road = map.getRoadBetween(trip.get(lastTripPos), trip.get(waitingPos));
			road.pushVehicle(this);

			location = 0;
		} else if (nextWaitingPos == trip.size()) {
			// Última vez. El cruce donde se espera es el destino final.
			hasArrived = true;
		} else {
			// Cambio normal de una Road a otra.
			road = map.getRoadBetween(trip.get(waitingPos), trip.get(nextWaitingPos));
			road.pushVehicle(this);
			location = 0;
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
		report.append( hasArrived ? "arrived" : "(" + road.getID() + ", " + location + ")");

		return report.toString();
	}
	
	/**
	 * Modifica el tiempo de avería.
	 * @param newBreakdownTime nuevo tiempo de avería
	 */
	public void setBreakdownTime(int newBreakdownTime)  {
		breakdownTime += newBreakdownTime;
	}	
	
	/**
	 * Modifica la velocidad de la carretera como el mínimo entre
	 * la velocidad que permite la Road y la velocidad máxima de Vehicle.
	 * @param roadSpeed velocidad permitida por la carretera
	 */
	public void setSpeed(int roadSpeed) {
		actualSpeed = Math.min(roadSpeed, maxSpeed);
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
}


