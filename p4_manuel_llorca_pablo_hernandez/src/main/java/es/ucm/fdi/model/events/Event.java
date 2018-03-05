package es.ucm.fdi.model.events;

public abstract class Event {
	
	private int time;
	
	public abstract void execute();

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
}
