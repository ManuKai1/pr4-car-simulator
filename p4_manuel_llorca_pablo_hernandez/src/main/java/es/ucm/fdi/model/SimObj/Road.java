package es.ucm.fdi.model.SimObj;

public class Road extends SimObject {
	final private String REPORT = "[road_report]";
	
	private int length;
	private int speedLimit;
	private Junction initialJunction;
	private Junction endJunction;
	
	//arraylist con lista de vehiculos ordenados por localizacion (FIFO), loc 0 = último
	
	
	public void pushVehicle(Vehicle v) {
		
		
	}
	
	public void popVehicle(Vehicle v) {
		
	}
	
	@Override
	public void proceed() {
		
	}
	
	@Override
	public String getReport(int simTime) {
		String info = "";
		
		info += REPORT + '\n';
		info += "id = " + id + '\n';
		info += "time = " + simTime + '\n';
		info += "state = ";
		//Bucle for de la lista de vehículos
		
		return info;
	}
	
	public int getLength() {
		return length;
	}

	
	public Junction getEndJunction() {
		return endJunction;
	}
	
	
}
