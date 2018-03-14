package es.ucm.fdi.control.evbuild;

import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.events.Event;
import es.ucm.fdi.model.events.FaultyVehicle;

public class FaultyVehicleBuilder extends EventBuilder{
	
	public FaultyVehicleBuilder(){
		super("make_vehicle_faulty");
	}
	
	@Override
	Event parse(IniSection ini) {
		if(ini.getTag().equals(iniName)){
			int time = 0;
			
			try{
				time = Integer.parseInt(ini.getValue("time"));
			}
			//El tiempo no era un entero
			catch(NumberFormatException e){
				throw new IllegalArgumentException("Time reading failure in faulty vehicles.");
			}
			//Comprobamos que el tiempo sea no negativo
			if(time < 0){
				throw new IllegalArgumentException("Negative time in faulty vehicles.");
			}
			
			int duration;
			try{
				duration = Integer.parseInt(ini.getValue("duration"));
			}
			//La duracion no era un entero
			catch(NumberFormatException e){
				throw new IllegalArgumentException("Duration reading failure in faulty vehicles.");
			}
			//Comprobamos que la duracion sea positiva
			if(duration <= 0){
				throw new IllegalArgumentException("Non-positive duration in faulty vehicles.");
			}
			
			//Lista de vehículos
			ArrayList<String> vehicles = new ArrayList<>();
			String line = ini.getValue("vehicles");
			String[] input = line.split(",");
			for(int i = 0; i < input.length; i++){
				if(!EventBuilder.validID(input[i])){
					throw new IllegalArgumentException("Illegal vehicle ID (number " + (i+1) + ")in faulty vehicles");
				}
				vehicles.add(input[i]);
			}
			//Tiene que haber al menos un vehiculo;
			if(vehicles.size() < 1){
				throw new IllegalArgumentException("Less than one vehicle in faulty vehicles.");
			}
			
			FaultyVehicle faulty = new FaultyVehicle(time, vehicles, duration);
			return faulty;
		}
		else return null;
	}

}
