package es.ucm.fdi.model.SimObj;

import java.util.ArrayList;

public class Vehicle extends SimObject {
	private final String REPORT_TITLE = "[vehicle_report]";
	
	//Puede ser mejor una cola y quitamos en los que ya hemos estado?
	private ArrayList<Junction> trip;
	private Road road;
	private int location;
	private int actualSpeed;
	private int maxSpeed;
	private int kilometrage;
	private int breakdownTime;
	private boolean hasArrived;
	private boolean isWaiting;	

	public boolean equals(Vehicle other) {
		if (id == other.id) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void proceed() {
		// //Comprobamos primero si el vehículo está averiado o no
		if (breakdownTime > 0) {
			breakdownTime--;
		}
		else {
			//Calculamos la nueva localización en la carretera del vehículo
			location += actualSpeed;
			if (location >= road.getLength()) {
				location = road.getLength();
				road.getEndJunction().pushVehicle(this);
				road.popVehicle(this);
				isWaiting = true;
			}
		}
		
	}
	
	@Override
	public String getReport(int simTime) {
		String info = "";
		
		info += REPORT_TITLE + '\n';
		info += "id = " + id + '\n';
		info += "time = " + simTime + '\n';
		info += "speed = " + actualSpeed + '\n';
		info += "kilometrage = " + kilometrage + '\n';
		info += "faulty = " + breakdownTime + '\n';
		info += "location = ";
		
		if(hasArrived) {
			info += "arrived";
		}
		else {
			info += '(' + road.getID() + ", " + location + ')';
		}
		info += '\n';
		return info;
	}
	
	public void moveToNextRoad() {
		//Función de búsqueda de carreteras que van a nuestro siguiente cruce.
		//Elegimos la única que coincide.
		
		//Primera vez: no hay carretera saliente.
		//Última vez: no hay carretera entrante.
		//hasArrived = true;
	}
	
	
	public void setBreakdownTime(int addBreakdownTime)  {
		breakdownTime += addBreakdownTime;
	}	
	
	public void setActualSpeed(int newActualSpeed)	{
		if(newActualSpeed > maxSpeed) {
			actualSpeed = maxSpeed;
		}
		else {
			actualSpeed = newActualSpeed;
		}
	}	
	
	public int getBreakdownTime(){
		return breakdownTime;
	}
	
	public boolean getIsWaiting(){
		return isWaiting;
	}
}


