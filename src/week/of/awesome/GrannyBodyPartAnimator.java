package week.of.awesome;

import com.badlogic.gdx.math.Vector2;

public class GrannyBodyPartAnimator {

	private static final float ANIM_RANGE = (float)Math.PI * 2f;
	
	private static final float ANIM_FREQ = ANIM_RANGE / 0.2f;
	
	private static final float LEG_HALF_HEIGHT_RANGE = 1f;
	
	private static final float LEG_CADENCE_OFFSET = ANIM_RANGE / 3f;
	
	private float time;
	
	private Vector2 shootAtPosition;
	private Vector2 gunPosition;
	
	public GrannyBodyPartAnimator(Granny granny, Vector2 gunPosition, Vector2 shootAtPosition) {
		time = granny.isAirborn() ? 0 : granny.getPosition().x;
		this.shootAtPosition = shootAtPosition;
		this.gunPosition = gunPosition;
	}
	
	public Vector2 getLeftLegPosition() {
		return new Vector2(-17, 18 + (float)Math.sin(time * ANIM_FREQ) * LEG_HALF_HEIGHT_RANGE);
	}
	
	public Vector2 getRightLegPosition() {
		return new Vector2(4, 18 + (float)Math.sin(time * ANIM_FREQ + LEG_CADENCE_OFFSET) * LEG_HALF_HEIGHT_RANGE);
	}
	
	public float getArmAngle() {
		return shootAtPosition.cpy().sub(gunPosition).angle();
	}
}
