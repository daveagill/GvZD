package week.of.awesome;

public class BulletDinoCollision extends Collision {

	private Bullet bullet;
	private Dino dino;
	
	public BulletDinoCollision(Bullet bullet, Dino dino) {
		this.bullet = bullet;
		this.dino = dino;
	}
	
	@Override
	public void triggerCollisionEvent(WorldEvents events) {
		events.dinoIsHitByBullet(dino, bullet);
	}

}
