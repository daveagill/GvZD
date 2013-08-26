package week.of.awesome;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Bullet {

	private int damage;
	private Body body;
	
	public Bullet(Body body, int damageInflicted) {
		this.body = body;
		body.setUserData(this);
		this.damage = damageInflicted;
	}
	
	public Body getBody() {
		return body;
	}
	
	public int getDamageInflicted() {
		return damage;
	}
	
	public Vector2 getPosition() {
		return body.getPosition().cpy();
	}
}
