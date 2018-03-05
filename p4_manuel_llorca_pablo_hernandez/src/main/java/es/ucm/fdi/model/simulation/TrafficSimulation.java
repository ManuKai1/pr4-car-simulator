package es.ucm.fdi.model.simulation;

import java.io.OutputStream;
import java.util.ArrayList;

import es.ucm.fdi.model.events.Event;

public class TrafficSimulation {

	private ArrayList<Event> events;
	//Estructura para guardar objetos simulados?
	//RoadMap
	private int time;
	
	//Ordenados por tiempo, y si es el mismo, orden de llegada
	public void pushEvent(Event e){
		
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
			informe +=;
			if(informe != ""){
				//Guardado de fichero
			}
		}
	}
	
}
