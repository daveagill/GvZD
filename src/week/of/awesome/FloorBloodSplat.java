package week.of.awesome;

import com.badlogic.gdx.math.Vector2;

public class FloorBloodSplat {

	private Vector2 position;
	private float rotation;
	
	public FloorBloodSplat(Vector2 position) {
		this.position = position;
		this.rotation = (float)Math.random() * 360f;
	}
	
	public Vector2 getPosition() {
		return position.cpy();
	}
	
	public float getRotation() {
		return rotation;
	}
}
