package es.ucm.fdi.control.evbuild;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.events.Event;
import es.ucm.fdi.model.events.NewJunction;

public class NewJunctionBuilder extends EventBuilder{
	
	public NewJunctionBuilder(){
		super("new_junction");
	}
	
	//Parser de NewJunction
	@Override
	Event parse(IniSection ini) throws IllegalArgumentException{
		//Comprobación de que es un NewJunction
		if(ini.getTag().equals(iniName)){
			String id = ini.getValue("id");
			int time = 0;
			
			//El ID sólo contiene letras,. números, o '_'
			if(!EventBuilder.validID(id)){
				throw new IllegalArgumentException("Illegal junction ID: " + id);
			}
			
			//Si se ha incluido la key time
			String timeKey = ini.getValue("time");
			if(timeKey != null){
				try{
					time = Integer.parseInt(timeKey);
				}
				//El tiempo no era un entero
				catch(NumberFormatException e){
					throw new IllegalArgumentException("Time reading failure in junction with ID: " + id);
				}
				//Comprobamos que el tiempo sea positivo
				if(time < 0){
					throw new IllegalArgumentException("Negative time in junction with ID: " + id);
				}
			}
			
			NewJunction junct = new NewJunction(time, id);
			return junct;
		}
		else return null;
	}

}
