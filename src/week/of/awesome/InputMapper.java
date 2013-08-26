package week.of.awesome;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class InputMapper {

	private WorldEvents events;
	private Input input;
	private Camera camera;
	
	private int jumpKey = Input.Keys.W;
	private int leftKey = Input.Keys.A;
	private int rightKey = Input.Keys.D;
	
	private int shootKey = Input.Keys.SPACE;
	
	public InputMapper(Input inputSystem, WorldEvents events, Camera camera) {
		this.events = events;
		this.input = inputSystem;
		this.camera = camera;
	}
	
	public void process() {
		if (input.isKeyPressed(jumpKey)) {
			events.grannyJump();
		}
		
		if (input.isKeyPressed(rightKey)) {
			events.moveGrannyRight();
		}
		
		if (input.isKeyPressed(leftKey)) {
			events.moveGrannyLeft();
		}
		
		// convert mouse coords to world coords
		Vector2 windowPos = new Vector2(input.getX(), input.getY());
		Vector2 screenPos = camera.windowCoordsToScreen(windowPos);
		Vector2 worldPos = camera.screenCoordsToWorld(screenPos);
		
		// call aimAt before shootAt, can't shoot without aiming
		events.aimAt(worldPos.x, worldPos.y);
		
		if (input.isKeyPressed(shootKey) || input.isTouched()) {
			events.shootAt(worldPos.x, worldPos.y);
		}
		
		
	}
}
