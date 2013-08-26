package week.of.awesome;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class AmmoPickup {

	private Body body;
	private int numRounds;
	
	public AmmoPickup(Body body, int numRounds) {
		this.body = body;
		body.setUserData(this);
		this.numRounds = numRounds;
	}
	
	public Body getBody() {
		return body;
	}
	
	public Vector2 getPosition() {
		return body.getPosition().cpy();
	}
	
	public int getNumRounds() {
		return numRounds;
	}
}
