package es.ucm.fdi.model.SimObj;

import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;

public class BikeVehicle extends Vehicle {

	public BikeVehicle(String identifier, ArrayList<Junction> trp, int max) {
		super(identifier, trp, max);
	}

	/**
	 * Modifica el tiempo de avería según las reglas de las bicicletas
	 * @param addedBreakdownTime tiempo de avería a sumar
	 */
	public void setBreakdownTime(int addedBreakdownTime)  {
		if(actualSpeed > maxSpeed / 2){
			breakdownTime += addedBreakdownTime;
		}
	}	
	
	/**
	 * Informe de la bike en cuestión, mostrando: id, tiempo de simulación, tipo bici
	 * velocidad actual, kilometraje, tiempo de avería, localización, llegada a
	 * destino
	 */
	@Override
	public String getReport(int simTime) {
		StringBuilder report = new StringBuilder();
		// TITLE
		report.append(REPORT_TITLE + '\n');
		// ID
		report.append("id = " + id + '\n');
		// SimTime
		report.append("time = " + simTime + '\n');
		// Type
		report.append("type = bike" + '\n');
		// Velocidad actual
		report.append("speed = " + actualSpeed + '\n');
		// Kilometraje
		report.append("kilometrage = " + kilometrage + '\n');
		// Tiempo de avería
		report.append("faulty = " + breakdownTime + '\n');
		// Localización
		report.append("location = ");
		report.append( hasArrived ? "arrived" : "(" + road.getID() + "," + location + ")");

		return report.toString();
	}
	
	/**
	 * A partir de los datos de la bike genera una IniSection
	 * @param simTime tiempo del simulador
	 * @return IniSection report de la bike
	 */
	public IniSection generateIniSection(int simTime){
		String tag = REPORT_TITLE;
		//Creación de etiqueta (sin corchetes)
		tag = (String) tag.subSequence(1, tag.length() - 1);
		IniSection section = new IniSection(tag);
		section.setValue("id", id);
		section.setValue("time", simTime);
		section.setValue("type", "bike");
		section.setValue("speed", actualSpeed);
		section.setValue("kilometrage", kilometrage);
		section.setValue("faulty", breakdownTime);
		section.setValue("location", hasArrived ? "arrived" : "(" + road.getID() + "," + location + ")");
		return section;
	}
	
}
