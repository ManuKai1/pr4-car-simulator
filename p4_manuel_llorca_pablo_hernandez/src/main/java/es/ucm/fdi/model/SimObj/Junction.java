package es.ucm.fdi.model.SimObj;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;

public class Junction extends SimObject {
	
	private final String REPORT_TITLE = "[junction_report]";
	
	//Métodos offer / poll
	Map<Road, IncomingRoad> roadToIncoming;
	ArrayList<IncomingRoad> incoming;	
	//Entero que mediante la operación módulo representa el semáforo encendido. Empieza en 0.
	int light;
	
	//Clase interna que representa una carretera entrante
	private static class IncomingRoad {
		private Road road;
		//True si el semáforo está en verde
		private boolean isGreen;
		private ArrayDeque<Vehicle> waiting = new ArrayDeque<>();
	}
	
	public Junction(String id) {
		
	}
	
	private void lightAdvance(){
		//Avanza en 1 el semáforo circular.
		light = (light + 1) % incoming.size();
	}
	
	@Override
	public void proceed() {
		if(!incoming.get(light).waiting.isEmpty()){
			//Si el avance es posible, el vehículo se elimina de la cola.
			//Visto en el enunciado; ¿por qué no iba a ser posible el avance?
			incoming.get(light).waiting.getFirst().moveToNextRoad();
		}
		incoming.get(light).isGreen = false;
		lightAdvance();
		incoming.get(light).isGreen = true;
	}
	
	//Función de report de cada deque
	private StringBuilder incomingReport(IncomingRoad actual){
		StringBuilder report = new StringBuilder();
		//ID
		report.append('(' + actual.road.getID() + ',');
		//Semáforo
		report.append(actual.isGreen ? "green" : "red");
		//Cola
		report.append(",[");
		if (actual.waiting.isEmpty()) {
			report.append("]");
		} 
		else {
			for (Vehicle v : actual.waiting) {
				report.append(v.id + ",");
			}
			report.setCharAt(report.length(), ']');
		}
		report.append(")");
		return report;
	}
	
	@Override
	public String getReport(int simTime) {
		StringBuilder report = new StringBuilder();
		report.append(REPORT_TITLE + '\n');
		report.append("id = " + id);
		report.append("time = " + simTime);
		report.append("queues = ");
		for(IncomingRoad incRoad : incoming){
			report.append(incomingReport(incRoad) + ", ");
		}
		//Borrado de última coma y espacio
		report.replace(report.length() - 1, report.length(), "\n");
		return report.toString();
	}
	
	public void pushVehicle(Vehicle v){
		//Buscar el incomingRoad de la road en Map
		IncomingRoad next = roadToIncoming.get(v.getRoad());
		//Añadir coche al final de la deque de incomingRoad
		next.waiting.addLast(v);
	}
	
	
	
	
}
