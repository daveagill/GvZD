package week.of.awesome;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Granny {	
	private Body body;
	private int health;
	private boolean airborn = true;
	private Facing facing = Facing.RIGHT;
	private int numKills = 0;
	private float timeToNextDamage = 0;
	
	public Granny(Body body, int health) {
		this.body = body;
		body.setUserData(this);
		this.health = health;
	}
	
	public void becomeAirborn(boolean airborn) {
		this.airborn = airborn;
	}
	
	public boolean isAirborn() {
		return airborn;
	}
	
	public Body getBody() { return body; }
	public Vector2 getPosition() { return body.getPosition().cpy(); }
	
	public void turnToFace(Facing facing) {
		this.facing = facing;
	}
	
	public Facing getFacingDirection() {
		return facing;
	}
	
	public void damage(int amount) {
		if (timeToNextDamage <= 0) {
			health -= amount;
			timeToNextDamage = 3; // wait 3 secs
			
			// make granny look hurt
			body.applyLinearImpulse(new Vector2(0, 5), body.getWorldCenter());
		}
	}
	public int getHealth() { return health; }
	public boolean dead() { return health <= 0; }
	
	public int getDistanceTravelled() { return (int)getPosition().x; }
	
	public void gainKill() {
		++numKills;
	}
	
	public int getKillCount() {
		return numKills;
	}
	
	public void jump() {
		if (!isAirborn()) {
			//body.applyLinearImpulse(new Vector2(0, 6f), body.getWorldCenter());
			body.setLinearVelocity(body.getLinearVelocity().x, 5f);
		}
	}
	
	public void moveRight() {
		body.setTransform(body.getTransform().getPosition().cpy().add(0.01f, 0), 0);
		body.setAwake(true);
	}
	
	public void moveLeft() {
		body.setTransform(body.getTransform().getPosition().cpy().add(-0.01f, 0), 0);
		body.setAwake(true);
	}
	
	public void update(float dt) {
		if (timeToNextDamage > 0) {
			timeToNextDamage -= dt;
		}
	}
}
