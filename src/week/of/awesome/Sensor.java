package week.of.awesome;

public abstract class Sensor {
	
	public static class FloorSensor extends Sensor { }
	
	private int numContacts = 0;
	
	public void makeContact() {
		++numContacts;
	}
	
	public void breakContact() {
		--numContacts;
	}
	
	public int getNumContacts() {
		return numContacts;
	}
	
	public boolean active() {
		return getNumContacts() > 0;
	}
}
