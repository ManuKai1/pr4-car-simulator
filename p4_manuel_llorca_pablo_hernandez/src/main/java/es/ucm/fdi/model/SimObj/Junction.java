package es.ucm.fdi.model.SimObj;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;

public class Junction extends SimObject {
	//queues, salidas con sus correspondientes semáforos y coches -> ArrayDeque
	//Métodos offer / poll
	Map<Road, IncomingRoad> roadToIncoming;
	ArrayList<IncomingRoad> incoming;	
	int light;
	
	private static class IncomingRoad {
		private Road road;
		private boolean isGreen; //segun el profe puede estar bien jejeje
		private ArrayDeque<Vehicle> waiting = new ArrayDeque<>();
	}
	
	public Junction(String id) {
		
	}
	
	
	
	@Override
	public void proceed() {
		
	}
	
	@Override
	public String getReport(int simTime) {
		
		return null;
	}
	
	public void pushVehicle(Vehicle v){
		//Introducir vehículo en queue FIFO
	}
	
	
	
	
}
