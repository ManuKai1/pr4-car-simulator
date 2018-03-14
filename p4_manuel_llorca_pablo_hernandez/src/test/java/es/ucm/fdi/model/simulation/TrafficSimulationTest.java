package es.ucm.fdi.model.simulation;

import java.io.OutputStream;
import java.util.ArrayList;

import org.junit.Test;

import es.ucm.fdi.model.events.FaultyVehicle;
import es.ucm.fdi.model.events.NewJunction;
import es.ucm.fdi.model.events.NewRoad;
import es.ucm.fdi.model.events.NewVehicle;
import static org.junit.Assert.*;

public class TrafficSimulationTest {
	
	@Test
	public void eventPushTest() throws Exception{
		
		TrafficSimulation sim = new TrafficSimulation();
		
		//Creación manual de eventos
		
		NewJunction newJ1 = new NewJunction(0, "j1");
		NewJunction newJ2 = new NewJunction(0, "j2");
		
		NewRoad newR1 = new NewRoad(0, "r1",  20, 10, "j1", "j2");
		
		ArrayList<String> trip = new ArrayList<>();
		trip.add("j1");
		trip.add("j2");
		NewVehicle newV1 = new NewVehicle(1, "v1", 5, trip);
		
		ArrayList<String> faulty = new ArrayList<>();
		faulty.add("v1");
		FaultyVehicle newFV = new FaultyVehicle(2, faulty, 3);
		
		//Adición de eventos al simulador
		sim.pushEvent(newJ1);
		sim.pushEvent(newJ2);
		sim.pushEvent(newR1);
		sim.pushEvent(newV1);
		sim.pushEvent(newFV);
		
		//No tenemos interes en guardar los resultados de la prueba: fichero null
		OutputStream file = null;
		sim.execute(1, file);
		
		//Aseguramos que se han creado los objetos que corresponden al timepo 0, y no los siguientes
		assertTrue(sim.roadMap.existsJunctionID("j1"));
		assertTrue(sim.roadMap.existsJunctionID("j2"));
		assertTrue(sim.roadMap.existsRoadID("r1"));
		assertFalse(sim.roadMap.existsVehicleID("v1"));
		
		sim.execute(1, file);
		
		//Aseguramos que se ha creado el vehículo que corresponde al tiempo 1
		assertTrue(sim.roadMap.existsVehicleID("v1"));
		
		sim.execute(1, file);
		
		//Aseguramos que se ha introducido el evento de avería
		//(de hecho, aprovechamos para comprobar que se ejecuta)
		assertEquals("no breakdown", 2, sim.roadMap.getVehicleWithID("v1").getBreakdownTime());
		
	}
	
}
