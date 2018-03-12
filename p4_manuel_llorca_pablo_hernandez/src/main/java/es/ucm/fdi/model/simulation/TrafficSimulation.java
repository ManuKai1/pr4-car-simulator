package es.ucm.fdi.model.simulation;

import java.io.OutputStream;

import es.ucm.fdi.model.events.Event;
import es.ucm.fdi.util.MultiTreeMap;

public class TrafficSimulation {

	private MultiTreeMap<Integer, Event> events;
	//Estructura para guardar objetos simulados?
	private RoadMap roadMap;
	private int time;
	
	//Función de añadir evento en función de su tiempo
	public void pushEvent(Event e)throws SimulationException{
		if(e.getTime() < time){
			throw new SimulationException("Event time is lower than current time.");
		}
		events.putValue(e.getTime(), e);
	}
	
	public void execute(int steps, OutputStream file){
		int timeLimit = time + steps - 1;
		while(time <= timeLimit){
			// 1. ejecutar los eventos correspondientes a ese tiempo
			//Implementación: usando arraylist?
			// 2. invocar al método avanzar de las carreteras
			// 3. invocar al método avanzar de los cruces
			// 4. this.contadorTiempo++;
			time++;
			// 5. esciribir un informe en OutputStream
			// en caso de que no sea nulo
			String informe = "";
			//Falta bucle que recorra estructuras en orden de entrada y junctions, roads, vehicles
			// informe +=;
			if(informe != ""){
				//Guardado de fichero
			}
		}
	}
	
}
