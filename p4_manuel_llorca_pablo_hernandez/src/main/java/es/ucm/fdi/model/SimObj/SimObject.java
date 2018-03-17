package es.ucm.fdi.model.SimObj;


//Clase padre del resto de: Vehicle, Road, Junction.
public abstract class SimObject { 
	protected String id;	
	
	public abstract void proceed();
	public abstract String getReport(int simTime); //simTime lo pasa el simulador.

	public SimObject(String identifier) {
		id = identifier;
	}
	
	public String getID() {
		return id;
	}
	
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimObject other = (SimObject) obj;
		return id == other.id;
	}
	
}
