package es.ucm.fdi.model.SimObj;

//Clase padre del resto de: Vehicle, Road, Junction.
public abstract class SimObject { 
	protected String id;	
	
	public abstract void proceed();
	public abstract String getReport(int simTime); //simTime lo pasa el simulador.
	
	public String getID() {
		return id;
	}
}
