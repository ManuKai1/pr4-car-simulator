package es.ucm.fdi.model.SimObj;

import java.util.ArrayList;
import java.util.Random;

import es.ucm.fdi.ini.IniSection;

public class CarVehicle extends Vehicle {
	//Resistencia a las averías
	private int resistance;
	//Probabilidad de avería
	private double faultyChance;
	//Duración máxima de avería
	private int faultDuration;
	//Semilla aleatoria
	private Random randomSeed;
	//Tiempo desde la última avería
	private int kmSinceFaulty;
	
	public CarVehicle(String identifier, ArrayList<Junction> trp, int max, int res,
			double breakChance, int breakDuration, long seed) {
		super(identifier, trp, max);
		resistance = res;
		faultyChance = breakChance;
		faultDuration = breakDuration;
		randomSeed = new Random(seed);
		kmSinceFaulty = 0;
	}
	
	public void proceed(){
		if(!isFaulty()){
			if(kmSinceFaulty > resistance){
				if(randomSeed.nextDouble() < faultyChance){
					// Se cumplen todas las condiciones de avería aleatoria
					// Generamos un tiempo de avería entre 1 y faultDuration
					setBreakdownTime(randomSeed.nextInt(faultDuration) + 1);
				}
			}
		}
		//Necesitamos volver a comprobarlo por si ha cambiado en el if anterior
		if(isFaulty()){
			kmSinceFaulty = 0;
			actualSpeed = 0;
		}
		//Guardamos km anteriores para conocer nueva distancia recorrida
		int oldKilometrage = kilometrage;
		super.proceed();
		kmSinceFaulty += kilometrage - oldKilometrage;
	}
	
	/**
	 * Informe del car en cuestión, mostrando: id, tiempo de simulación, tipo coche
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
		report.append("type = car" + '\n');
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
	 * A partir de los datos del car genera una IniSection
	 * @param simTime tiempo del simulador
	 * @return IniSection report del car
	 */
	public IniSection generateIniSection(int simTime){
		String tag = REPORT_TITLE;
		//Creación de etiqueta (sin corchetes)
		tag = (String) tag.subSequence(1, tag.length() - 1);
		IniSection section = new IniSection(tag);
		section.setValue("id", id);
		section.setValue("time", simTime);
		section.setValue("type", "car");
		section.setValue("speed", actualSpeed);
		section.setValue("kilometrage", kilometrage);
		section.setValue("faulty", breakdownTime);
		section.setValue("location", hasArrived ? "arrived" : "(" + road.getID() + "," + location + ")");
		return section;
	}
	
	
}
