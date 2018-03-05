package es.ucm.fdi.model.SimObj;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Road extends SimObject {
	final private String REPORT_TITLE = "[road_report]";
	
	private Junction initialJunction;
	private Junction endJunction;
	private int length;
	private int speedLimit;
	private ArrayList<Vehicle> vehiclesOnRoad;
	
	public Road(String id) {
		
		
	}
	
	@Override
	public void proceed() {
		int baseSpeed;
		//Cálculo de velocidadBase según la fórmula
		baseSpeed = Math.min(speedLimit, ((speedLimit)/Math.max(vehiclesOnRoad.size(), 1)) + 1);
		int reductionFactor = 1;
		//Recorrido de lista de vehículos
		for(int i = 0; i < vehiclesOnRoad.size(); i++){
			//Vehículo actual
			Vehicle vehicleNow = vehiclesOnRoad.get(i);
			//Modificamos su velocidad, lo hacemos avanzar, y si está roto cambiamos el reductionFactor
			vehicleNow.setActualSpeed(baseSpeed / reductionFactor);
			vehicleNow.proceed();
			if(vehicleNow.getBreakdownTime() > 0){
				reductionFactor = 2;
			}
			//Necesario para correcto recorrido del array en caso de que haya pop de un vehículo.
			if(vehicleNow.getIsWaiting()){
				i--;
			}
		}
	}
	
	//Función de report de cada vehículo
	private StringBuilder vehicleReport(Vehicle v){
		StringBuilder report = new StringBuilder();
		report.append("(" + v.getID() + "," + v.getLocation() + "), ");
		return report;
	}
	
	@Override
	public String getReport(int simTime) {
		StringBuilder report = new StringBuilder();
		
		report.append(REPORT_TITLE + '\n');
		report.append("id = " + id + '\n');
		report.append("time = " + simTime + '\n');
		report.append("state = ");
		//Si no hay vehículos no necesitamos escribir nada
		if(!vehiclesOnRoad.isEmpty()){
			for(Vehicle v : vehiclesOnRoad){
				report.append(vehicleReport(v));
			}
			//Borrado de última coma y espacio
			report.replace(report.length() - 1, report.length(), "");
		}
		report.append("\n");
		return report.toString();
	}
	
	public void pushVehicle(Vehicle v) {
		vehiclesOnRoad.add(v);
	}
	
	public void popVehicle(Vehicle v) throws NoSuchElementException{
		if (!vehiclesOnRoad.remove(v)) {
			throw new NoSuchElementException("Vehicle to pop not found.");
		}
	}
		
	public int getLength() {
		return length;
	}

	public Junction getEndJunction() {
		return endJunction;
	}
	
	
}
