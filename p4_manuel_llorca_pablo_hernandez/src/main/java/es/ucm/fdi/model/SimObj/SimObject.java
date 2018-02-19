package es.ucm.fdi.model.SimObj;

public abstract class SimObject { //Hacer clase madre del resto de: Vehicle, Road, Junction.
	protected String id;	
	
	public abstract void proceed();
	public abstract String getReport(int simTime); //simTime lo pasa el simulador.
	
	public String getID() {
		return id;
	}
}
