package week.of.awesome;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Dino {
	
	public static enum Kind { REX, ZOMBIE_REX, DIPLO, ZOMBIE_DIPLO, TRITOPS, ZOMBIE_TRITOPS, STEGO, ZOMBIE_STEGO };

	private Kind kind;
	private Body body;
	private int health;
	private float impulse;
	private Vector2 jumpVector;
	private float lookAhead;
	private int damageInflictedOnGranny;
	private Facing facing;
	private float age;
	private float previousX;
	
	public Dino(Kind kind, Body body, int health, float width, float impulse, Vector2 jumpVector, int damageInflictedOnGranny) {
		this.kind = kind;
		this.body = body;
		body.setUserData(this);
		this.health = health;
		this.impulse = impulse;
		this.jumpVector = jumpVector;
		this.lookAhead = width / 1.5f;
		this.damageInflictedOnGranny = damageInflictedOnGranny;
		this.previousX = body.getPosition().x-1;
	}
	
	public Body getBody() { return body; }
	public Kind getKind() { return kind; }
	
	public Vector2 getPosition() { return body.getPosition().cpy(); }
	
	public void damage(int amount) {
		health -= amount;
	}
	public int getHealth() { return health; }
	public boolean dead() { return health <= 0; }
	
	public Facing getFacingDirection() { return facing; }
	
	public int getDamageInflictedOnGranny() {
		return damageInflictedOnGranny;
	}
	
	public float getAge() {
		return age;
	}
	
	public void navigate(World world, float dt) {
		age += dt;
		
		float sign = 1;
		facing = Facing.RIGHT;
		
		if (world.getGranny().getPosition().x < body.getPosition().x) {
			sign = -1;
			facing = Facing.LEFT;
		}
		
		if (body.getLinearVelocity().cpy().mul(sign).x < impulse ) {
			body.applyLinearImpulse(new Vector2(impulse*sign, 0), body.getWorldCenter());
		}
		
		// make dino jump when he reaches a step or if he gets stuck
		boolean stuck = Math.abs(previousX - getPosition().x) < 0.001f;
		float floorHeightAhead = world.getFloorHeightAt(getPosition().x + lookAhead * sign);
		float wallHeight = floorHeightAhead - getPosition().y;
		boolean jumpable = wallHeight < Stage.BLOCK_SIZE * 1.5f && wallHeight > Stage.BLOCK_SIZE * 0.1f;
		if (body.getLinearVelocity().y == 0 && (jumpable || stuck)) {
			body.applyLinearImpulse(jumpVector.cpy().mul(sign, 1), body.getWorldCenter());
		}
		
		previousX = getPosition().x;
	}
}
