package es.ucm.fdi.control.evbuild;

import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.events.Event;
import es.ucm.fdi.model.events.NewVehicle;

public class NewVehicleBuilder extends EventBuilder{

	public NewVehicleBuilder(){
		super("new_vehicle");
	}
	
	@Override
	Event parse(IniSection ini) {
		if(ini.getTag().equals(iniName) && ini.getValue("type") == null){
			String id = ini.getValue("id");
			int time = 0;
			int maxSpeed;
			
			//El ID sólo contiene letras,. números, o '_'
			if(!EventBuilder.validID(id)){
				throw new IllegalArgumentException("Illegal vehicle ID: " + id);
			}
			
			String timeKey = ini.getValue("time");
			if(timeKey != null){
				try{
					time = Integer.parseInt(timeKey);
				}
				//El tiempo no era un entero
				catch(NumberFormatException e){
					throw new IllegalArgumentException("Time reading failure in vehicle with ID: " + id);
				}
				//Comprobamos que el tiempo sea no negativo
				if(time < 0){
					throw new IllegalArgumentException("Negative time in vehicle with ID: " + id);
				}
			}
			
			try{
				maxSpeed = Integer.parseInt(ini.getValue("max_speed"));
			}
			//La velocidad no era un entero
			catch(NumberFormatException e){
				throw new IllegalArgumentException("Max speed reading failure in vehicle with ID: " + id);
			}
			//Comprobamos que la velocidad sea positiva
			if(maxSpeed <= 0){
				throw new IllegalArgumentException("Non-positive speed in vehicle with ID: " + id);
			}
			
			//Itinerario del vehículo
			ArrayList<String> trip = new ArrayList<>();
			String line = ini.getValue("itinerary");
			String[] input = line.split(",");
			for(int i = 0; i < input.length; i++){
				if(!EventBuilder.validID(input[i])){
					throw new IllegalArgumentException("Illegal junction ID (number " + (i+1) + ")in vehicle with ID: " + id);
				}
				trip.add(input[i]);
			}
			//Tienen que haber al menos 2 junctions;
			if(trip.size() < 2){
				throw new IllegalArgumentException("Less than two junctions in vehicle with ID: " + id);
			}
			
			NewVehicle vehicle = new NewVehicle(time, id, maxSpeed, trip);
			return vehicle;
		}
		else return null;
	}

}
