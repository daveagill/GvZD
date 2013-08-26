package week.of.awesome;

public class FloorSensorCollision extends Collision {

	private Object entity;
	private Sensor sensor;
	
	public FloorSensorCollision(Object entity, Sensor sensor) {
		this.entity = entity;
		this.sensor = sensor;
	}
	
	@Override
	public void triggerCollisionEvent(WorldEvents events) {
		boolean landing = !sensor.active();
		sensor.makeContact();
		if (landing) {
			if (entity instanceof Granny) {
				events.grannyLands();
			}
		}
	}

	@Override
	public void triggerCollisionEndedEvent(WorldEvents events) {
		sensor.breakContact();
		if (!sensor.active()) {
			if (entity instanceof Granny) {
				events.grannyAirborn();
			}
		}
	}
}
