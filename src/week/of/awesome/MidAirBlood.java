package week.of.awesome;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class MidAirBlood {

	private Body body;
	private boolean willSplat;
	
	public MidAirBlood(Body body, boolean willSplat) {
		this.body = body;
		body.setUserData(this);
		this.willSplat = willSplat;
	}
	
	public Body getBody() {
		return body;
	}
	
	public Vector2 getPosition() {
		return body.getPosition().cpy();
	}
	
	public boolean shouldSplat() {
		return willSplat;
	}
}
