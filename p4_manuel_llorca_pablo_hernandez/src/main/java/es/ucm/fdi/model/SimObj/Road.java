package es.ucm.fdi.model.SimObj;

public class Road extends SimObject {
	final private String REPORT = "[road_report]";
	
	private Junction initialJunction;
	private Junction endJunction;
	private int length;
	private int speedLimit;
	//arraylist con lista de vehiculos ordenados por localizacion (FIFO), loc 0 = último
	
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
	
	public void pushVehicle(Vehicle v) {
		
	}
	
	public void popVehicle(Vehicle v) {
		
	}
		
	public int getLength() {
		return length;
	}

	public Junction getEndJunction() {
		return endJunction;
	}
	
	
}
