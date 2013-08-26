package week.of.awesome;

public class MidAirBloodFloorCollision extends Collision {

	private MidAirBlood blood;
	
	public MidAirBloodFloorCollision(MidAirBlood blood) {
		this.blood = blood;
	}
	
	@Override
	public void triggerCollisionEvent(WorldEvents events) {
		events.midAirBloodSplatsOnGround(blood);
	}

}
