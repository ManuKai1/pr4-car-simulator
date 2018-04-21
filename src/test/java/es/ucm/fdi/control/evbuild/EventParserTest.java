package es.ucm.fdi.control.evbuild;

import java.util.ArrayList;

import org.junit.Test;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.events.Event;
import es.ucm.fdi.model.events.FaultyVehicle;
import es.ucm.fdi.model.events.NewJunction;
import es.ucm.fdi.model.events.NewRoad;
import es.ucm.fdi.model.events.NewVehicle;

import static org.junit.Assert.*;

/**
 *	Unit Test for EventParser
 */
public class EventParserTest {

	@Test
	// Existe la posibilidad de hacer un test por cada event.
	// Se usa un solo test pasa asegurar que mantener una sola
	// instancia del parser no hace que falle.
	public void parseEvents() throws Exception{
		
		NewJunction j1 = new NewJunction(0, "j1");
		
		NewRoad r1 = new NewRoad(3, "r1", 10, 5, "j1", "j2");
		
		ArrayList<String> trip = new ArrayList<>();
		trip.add("j1");
		trip.add("j2");
		NewVehicle v1 = new NewVehicle(4, "v1", 4, trip);
		
		ArrayList<String> broken = new ArrayList<>();
		broken.add("v1");
		FaultyVehicle fv = new FaultyVehicle(5, broken, 3);
		
		EventParser parser = new EventParser();
		Event current;
		
		//Testeo de NewJunctionBuilder
		IniSection iniJ1 = new IniSection("new_junction");
		iniJ1.setValue("id", "j1");
		current = parser.parse(iniJ1);
		assertEquals("junction builder test", j1, current);
		
		//Testeo de NewRoadBuilder
		IniSection iniR1 = new IniSection("new_road");
		iniR1.setValue("time", 3);
		iniR1.setValue("id", "r1");
		iniR1.setValue("src", "j1");
		iniR1.setValue("dest", "j2");
		iniR1.setValue("max_speed", 5);
		iniR1.setValue("length", 10);
		current = parser.parse(iniR1);
		assertEquals("road builder test", r1, current);
		
		//Testeo de NewVehicleBuilder
		IniSection iniV1 = new IniSection("new_vehicle");
		iniV1.setValue("time", 4);
		iniV1.setValue("id", "v1");
		iniV1.setValue("max_speed", 4);
		iniV1.setValue("itinerary", "j1,j2");
		current = parser.parse(iniV1);
		assertEquals("vehicle builder test", v1, current);
		
		//Testeo de FaultyVehicleBuilder
		IniSection iniFV = new IniSection("make_vehicle_faulty");
		iniFV.setValue("time", 5);
		iniFV.setValue("vehicles", "v1");
		iniFV.setValue("duration", 3);
		current = parser.parse(iniFV);
		assertEquals("faulty vehicle builder test", fv, current);
	}
	
}
