package week.of.awesome;

public class GrannyDinoCollision extends Collision {

	private Dino dino;
	
	public GrannyDinoCollision(Dino dino) {
		this.dino = dino;
	}
	
	@Override
	public void triggerCollisionEvent(WorldEvents events) {
		events.grannyIsHitByDino(dino);
	}

}
