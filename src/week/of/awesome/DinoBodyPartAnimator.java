package week.of.awesome;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class DinoBodyPartAnimator {

	private static final float ANIM_RANGE = (float)Math.PI * 2f;
	
	private static final float ANIM_FREQ = ANIM_RANGE / 3f;
	
	private static final float LEG_HALF_ANGLE_RANGE = 20f;
	
	private static final float LEG_CADENCE_OFFSET = ANIM_RANGE / 3f;
	
	private float time;
	private static final float legY = 85;//45;
	private static final float halfLegX = 50;//25;
	
	public DinoBodyPartAnimator(Dino dino) {
		time = dino.getAge();
	}
	
	public Vector2 getLeftLegPosition() {
		return new Vector2(-halfLegX, legY);
	}
	
	public Vector2 getRightLegPosition() {
		return new Vector2(halfLegX, legY);
	}
	
	public float getLeftLegAngle() {
		return (float)Math.sin(time * ANIM_FREQ) * LEG_HALF_ANGLE_RANGE;
	}
	
	public float getRightLegAngle() {
		return (float)Math.sin(time * ANIM_FREQ + LEG_CADENCE_OFFSET) * LEG_HALF_ANGLE_RANGE;
	}
	
	public Vector2 getTopArmPosition() {
		return new Vector2();
	}
	
	public Vector2 getBottomArmPosition() {
		return new Vector2();
	}
	
	public float getTopArmAngle() {
		return 0;
	}
	
	public float getBottomArmAngle() {
		return 0;
	}
}
