package week.of.awesome;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.math.Vector2;

public class Camera {
	
	public static float SCREEN_UNITS_PER_WORLD_UNIT = 80.0f;
	public static float WORLD_UNITS_PER_SCREEN_UNIT = 1f / SCREEN_UNITS_PER_WORLD_UNIT;

	private Vector2 position = new Vector2(0, 0);
	private int pixelHeight;
	private float worldWidth;
	
	public Camera(int screenWidth, int screenHeight) {
		pixelHeight = screenHeight;
		worldWidth = stow(screenWidth);
	}
	
	
	public void lookAt(float x, float y) {
		position.set(x, y);
	}
	
	public void scrollTo(Vector2 pos) {
		Vector2 cameraSpacePos = pos.cpy().sub(position);
		
		float grannyScrolledRightAmount = (cameraSpacePos.x - 6);
		if (grannyScrolledRightAmount > 0) {
			position.x += grannyScrolledRightAmount;
		}
		
		float grannyScrolledLeftAmount = (cameraSpacePos.x - 1);
		if (grannyScrolledLeftAmount < 0) {
			position.x += grannyScrolledLeftAmount;
		}
	}
	
	public void pan(float x, float y) {
		position.add(x, y);
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public float getWorldWidth() {
		return worldWidth;
	}
	
	public static float stow(float screen) {
		return screen * WORLD_UNITS_PER_SCREEN_UNIT;
	}
	
	public static float wtos(float worldUnits) {
		return worldUnits * SCREEN_UNITS_PER_WORLD_UNIT;
	}
	
	public static Vector2 stow(Vector2 screen) {
		float x = stow(screen.x);
		float y = stow(screen.y);
		return new Vector2(x, y);
	}
	
	
	public Vector2 windowCoordsToScreen(Vector2 window) {
		return new Vector2(window.x, pixelHeight - window.y);
	}

	public Vector2 screenCoordsToWorld(Vector2 screen) {
		return stow(screen).add(position);
	}
	
	public boolean isClipped(Vector2 point) {
		final float clipDistance = 20;
		return point.dst2(position) > clipDistance * clipDistance;
	}
}
