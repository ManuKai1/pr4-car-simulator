package es.ucm.fdi.model.SimObj;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;

public class Junction extends SimObject {
	//Métodos offer / poll
	Map<Road, IncomingRoad> roadToIncoming;
	ArrayList<IncomingRoad> incoming;	
	//Entero que mediante la operación módulo representa el semáforo encendido. Empieza en 0.
	int light;
	
	//Clase interna que representa una carretera entrante
	private static class IncomingRoad {
		private Road road;
		private boolean isGreen; //segun el profe puede estar bien jejeje
		private ArrayDeque<Vehicle> waiting = new ArrayDeque<>();
	}
	
	public Junction(String id) {
		
	}
	
	private void lightAdvance(){
		light = (light + 1) % incoming.size();
	}
	
	@Override
	public void proceed() {
		if(!incoming.get(light).waiting.isEmpty()){
			//Si el avance es posible, el vehículo se elimina de la cola.
			//Visto en el enunciado; ¿por qué no iba a ser posible el avance?
			incoming.get(light).waiting.getFirst().moveToNextRoad();
		}
		lightAdvance();
	}
	
	@Override
	public String getReport(int simTime) {
		
		return null;
	}
	
	public void pushVehicle(Vehicle v){
		//Buscar el incomingRoad de la road en Map
		//Añadir coche al final de la deque de incomingRoad
	}
	
	
	
	
}
