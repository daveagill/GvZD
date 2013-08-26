package week.of.awesome;

import com.badlogic.gdx.math.Vector2;

public class WorldEvents {
	
	private static final float CHECKPOINT_DISTANCE = 8;
	private static final float AMMO_SPAWN_DISTANCE = 10;

	private World world = null;
	private PlayingGameState gameState;
	
	private SoundEffects sounds;
	
	public WorldEvents(PlayingGameState gameState, SoundEffects sounds) {
		this.gameState = gameState;
		this.sounds = sounds;
	}
	
	public void resetWorld(World world) {
		this.world = world;
	}
	
	public void cameraReachedNextLevel(Level nextLevel) {
		// set the checkpoint for granny and spawn some ammo, all offscreen
		boolean checkpointIsSignpost = nextLevel.getSignPostFilename() != null;
		world.setCheckpointX( nextLevel.getStartX() + CHECKPOINT_DISTANCE, checkpointIsSignpost );
		world.spawnAmmoPickup(new Vector2(nextLevel.getStartX() + AMMO_SPAWN_DISTANCE, 5), 1000);
	}
	
	public void grannyReachedCheckpoint(Level level) {
		if (level.getSignPostFilename() != null) {
			gameState.beginReadingSignpost();
		}
	}
	
	public void reachedAmmoDrop(float ammoDropX) {
		world.spawnAmmoPickup(new Vector2(ammoDropX + AMMO_SPAWN_DISTANCE, 5), 1000);
	}
	
	public void grannyJump() {
		world.getGranny().jump();
	}
	
	public void grannyAirborn() {
		world.getGranny().becomeAirborn(true);
	}
	
	public void grannyLands() {
		world.getGranny().becomeAirborn(false);
	}

	public void moveGrannyRight() {
		world.getGranny().moveRight();
	}

	public void moveGrannyLeft() {
		world.getGranny().moveLeft();
	}
	
	public void aimAt(float x, float y) {
		if (x >= world.getGranny().getPosition().x) {
			world.getGranny().turnToFace(Facing.RIGHT);
		}
		else {
			world.getGranny().turnToFace(Facing.LEFT);
		}
		
		world.setAimAtPosition(new Vector2(x, y));
	}

	public void shootAt(float x, float y) {
		if (world.getAmmoRemaining() > 0) {
		
			final float speed = 5;
			Vector2 velocity = new Vector2(x, y).sub(world.getGunLocation()).nor().mul(speed);
			
			world.spawnBullet(velocity, 1);
		}
		
		world.setShootAtPosition(new Vector2(x, y));
	}
	
	public void bulletIsShot() {
		sounds.shoot();
	}
	
	public void dinoIsHitByBullet(Dino dino, Bullet bullet) {
		if (dino.dead()) { return; }
		
		dino.damage(bullet.getDamageInflicted());
		
		if (dino.dead()) {
			world.removeDino(dino);
			world.getGranny().gainKill();
			sounds.dinoDeath();
		}
		
		world.removeBullet(bullet);
		
		world.spawnMidAirBlood(bullet.getPosition(), bullet.getBody().getLinearVelocity(), 5);
	}
	
	public void grannyIsHitByDino(Dino dino) {
		world.getGranny().damage(dino.getDamageInflictedOnGranny());
		
		float pushDirection = dino.getFacingDirection() == Facing.RIGHT ? -1 : 1;
		Vector2 pushImpulse = new Vector2(5*pushDirection, 3);
		dino.getBody().setLinearVelocity(pushImpulse);
	}
	
	public void midAirBloodSplatsOnGround(MidAirBlood blood) {
		world.splatBloodToGround(blood);
	}
	
	public void pickupAmmo(AmmoPickup ammo) {
		world.pickupAmmo(ammo);
	}
}
