package es.ucm.fdi.model.SimObj;

public class Vehicle extends SimObject {
	final private String REPORT = "[vehicle_report]";
	
	private int maxSpeed;
	private int actualSpeed;
	private Road road;
	private int location;
	private int kilometrage;
	// private arraylist de Junction
	private int breakdownTime;
	
	private boolean hasArrived;
	private boolean isWaiting;

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
	
	public void moveToNextRoad() {
		//Función de búsqueda de carreteras que salen de nuestro cruce actual.
		//Sobre ellas, función de búsqueda de nuestro cruce deseado.
		//Elegimos la única que coincide.
		
		//Primera vez: no hay carretera saliente.
		//Última vez: no hay carretera entrante.
		//hasArrived = true;
	}
	
	
	public void setBreakdownTime(int addBreakdownTime)  {
		breakdownTime += addBreakdownTime;
	}	
	
	public void setActualSpeed(int newActualSpeed)	{
		if(newActualSpeed > maxSpeed){
			actualSpeed = maxSpeed;
		}
		else{
			actualSpeed = newActualSpeed;
		}
	}	

	@Override
	public String getReport(int simTime) {
		String info = "";
		
		info += REPORT + '\n';
		info += "id = " + id + '\n';
		info += "time = " + simTime + '\n';
		info += "speed = " + actualSpeed + '\n';
		info += "kilometrage = " + kilometrage + '\n';
		info += "faulty = " + breakdownTime + '\n';
		info += "location = ";
		if(hasArrived){
			info += "arrived";
		}
		else{
			info += '(' + road.getID() + ", " + location + ')';
		}
		info += '\n';
		return info;
	}
}


