package es.ucm.fdi.model.SimObj;

public class Junction extends SimObject {
	//queues, salidas con sus correspondientes semáforos y coches -> ArrayDeque
	//Métodos offer / poll
	
	@Override
	public void proceed(){
		
	}
	
	@Override
	public String getReport(int simTime) {
		
		return null;
	}
	
	public void pushVehicle(Vehicle v){
		//Introducir vehículo en queue FIFO
	}
	
	
}
