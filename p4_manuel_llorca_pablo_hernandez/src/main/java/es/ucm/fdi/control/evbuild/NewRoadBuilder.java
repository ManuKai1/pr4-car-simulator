package es.ucm.fdi.control.evbuild;


import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.events.Event;
import es.ucm.fdi.model.events.NewRoad;

public class NewRoadBuilder extends EventBuilder{
	
	public NewRoadBuilder(){
		super("new_road");
	}
	
	@Override
	Event parse(IniSection ini) {
		if(ini.getTag().equals(iniName)){
			String id = ini.getValue("id");
			int time;
			int maxSpeed;
			int length;
			
			//El ID sólo contiene letras,. números, o '_'
			if(!EventBuilder.validID(id)){
				throw new IllegalArgumentException("Illegal road ID: " + id);
			}
			
			try{
				time = Integer.parseInt(ini.getValue("time"));
			}
			//El tiempo no era un entero
			catch(NumberFormatException e){
				throw new IllegalArgumentException("Time reading failure in road with ID: " + id);
			}
			//Comprobamos que el tiempo sea no negativo
			if(time < 0){
				throw new IllegalArgumentException("Negative time in road with ID: " + id);
			}
			
			String src = ini.getValue("src");
			if(!EventBuilder.validID(src)){
				throw new IllegalArgumentException("Illegal source junction ID in road with ID: " + id);
			}
			
			String dest = ini.getValue("dest");
			if(!EventBuilder.validID(dest)){
				throw new IllegalArgumentException("Illegal destination junction ID in road with ID: " + id);
			}
			
			try{
				maxSpeed = Integer.parseInt(ini.getValue("max_speed"));
			}
			//La velocidad no era un entero
			catch(NumberFormatException e){
				throw new IllegalArgumentException("Max speed reading failure in road with ID: " + id);
			}
			//Comprobamos que la velocidad sea positiva
			if(maxSpeed <= 0){
				throw new IllegalArgumentException("Non-positive speed in road with ID: " + id);
			}
			
			try{
				length = Integer.parseInt(ini.getValue("length"));
			}
			//La longitud no era un entero
			catch(NumberFormatException e){
				throw new IllegalArgumentException("Length reading failure in road with ID: " + id);
			}
			//Comprobamos que la longitud sea positiva
			if(length <= 0){
				throw new IllegalArgumentException("Non-positive length in road with ID: " + id);
			}
			
			NewRoad road = new NewRoad(time, id, length, maxSpeed, src, dest);
			return road;
			
		}
		else return null;
	}

}
