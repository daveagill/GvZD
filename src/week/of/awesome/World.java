package week.of.awesome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class World {
	
	private Camera camera;
	private Granny granny;
	private List<Bullet> bullets = new ArrayList<Bullet>();
	private List<Dino> dinos = new ArrayList<Dino>();
	private List<MidAirBlood> midAirBlood = new ArrayList<MidAirBlood>();
	private List<FloorBloodSplat> floorBloodSplats = new ArrayList<FloorBloodSplat>();
	private List<AmmoPickup> ammoPickups = new ArrayList<AmmoPickup>();
	
	private List<Level> futureLevels = new ArrayList<Level>();
	private Level currentLevel;
	private Level nextLevelForGrannyToReach;
	private Level nextLevelForCameraToReach;
	private float checkpointX = 0;
	private boolean checkpointIsSignpost = false;
	
	private final float bulletSpawnRate = 1f/10f;
	
	private int ammoRemaining = 0;
	
	private boolean gunWasShot = false;
	private Vector2 shootAtPos = null;
	private Vector2 aimPos = new Vector2(0, 0);
	
	private Vector2 scheduledBulletSpawnPos = null;
	private Vector2 scheduledBulletSpawnVelocity = null;
	private float timeSinceLastShot = 0;
	
	private float timeSinceLastLeftDinoSpawn = 0;
	private float timeSinceLastRightDinoSpawn = 0;
	
	private int numLeftDinos = 0;
	private int numRightDinos = 0;
	
	private float nextAmmoDropX = 0;
	
	private static final int MAX_DINOS_PER_SIDE = 10;
	private static final int MAX_FLOOR_BLOOD = 500;

	private Physics physics = new Physics();
	private HeightMapStage stage = new HeightMapStage(physics);
	
	public World(Camera camera) {
		this.camera = camera;
	}
	
	public void dispose() {
		// must dispose of stage before physics
		stage.dispose();
		physics.dispose();
	}
	
	public com.badlogic.gdx.physics.box2d.World getB2d() {
		return physics.getB2d();
	}
	
	public void spawnLeftDino() {
		if (numLeftDinos >= MAX_DINOS_PER_SIDE) { return; }
		float spawnX = camera.getPosition().x - 2;
		float spawnY = stage.heightAt(spawnX) + 1;
		dinos.add(createDino(currentLevel.getDinoKind(), new Vector2(spawnX, spawnY)));
		++numLeftDinos;
	}
	
	public void spawnRightDino() {
		if (numRightDinos >= MAX_DINOS_PER_SIDE) { return; }
		float spawnX = camera.getPosition().x + 13;
		float spawnY = stage.heightAt(spawnX) + 1;
		dinos.add(createDino(currentLevel.getDinoKind(), new Vector2(spawnX, spawnY)));
		++numRightDinos;
	}
	
	public void removeDino(Dino dino) {
		dinos.remove(dino);
		physics.removeBody(dino.getBody());
	}
	
	public void removeBullet(Bullet bullet) {
		bullets.remove(bullet);
		physics.removeBody(bullet.getBody());
	}
	
	public void spawnMidAirBlood(Vector2 position, Vector2 velocity, int quantity) {
		final float spreadRange = 2;
		for (int i = 0; i < quantity; ++i) {
			float tweak = (float)(Math.random()-0.5)*spreadRange;
			Vector2 tweakedVelocity = velocity.cpy().add(tweak/2, tweak).mul(0.5f, 1);
			boolean shouldSplat = i == 0;
			midAirBlood.add(createMidAirBlood(position, tweakedVelocity, shouldSplat));
		}
	}
	
	public void splatBloodToGround(MidAirBlood blood) {
		midAirBlood.remove(blood);
		physics.removeBody(blood.getBody());
		if (blood.shouldSplat()) {
			spawnBloodSplat(blood.getPosition().sub(0, (float)Math.random() * 0.3f));
		}
	}
	
	public void spawnBloodSplat(Vector2 position) {
		if (floorBloodSplats.size() == MAX_FLOOR_BLOOD) {
			floorBloodSplats.remove(0);
		}
		floorBloodSplats.add(new FloorBloodSplat(position));//.cpy().sub(0, 0.5f));
	}
	
	public void spawnBullet(Vector2 velocity, float spawnRadiusFromGranny) {
		if (ammoRemaining <= 0) { return; }
		
		Vector2 spawnPos = velocity.cpy().nor().mul(spawnRadiusFromGranny).add(getGunLocation());
		scheduledBulletSpawnPos = spawnPos;
		scheduledBulletSpawnVelocity = velocity;
		ammoRemaining -= 1;
	}
	
	public void pickupAmmo(AmmoPickup ammo) {
		ammoRemaining += ammo.getNumRounds();
		ammoPickups.remove(ammo);
		physics.removeBody(ammo.getBody());
	}
	
	public void setAmmoRemaining(int ammoAmount) {
		ammoRemaining = ammoAmount;
	}
	
	public int getAmmoRemaining() {
		return ammoRemaining;
	}
	
	public Vector2 getShootAtPosition() {
		return shootAtPos;
	}
	
	public void setShootAtPosition(Vector2 shootAt) {
		gunWasShot = true;
		this.shootAtPos = shootAt;
	}
	
	public void setAimAtPosition(Vector2 aimPos) {
		this.aimPos = aimPos;
	}
	
	public Vector2 getAimAtPosition() {
		return aimPos;
	}
	
	public void spawnAmmoPickup(Vector2 position, int numRounds) {
		ammoPickups.add(createAmmoPickup(position, numRounds));
	}
	
	public void respawnGranny() {
		granny = createGranny(new Vector2(3, 10));
	}
	
	public void registerLevel(Level level) {
		if (currentLevel == null) {
			currentLevel = level;
			nextLevelForGrannyToReach = level;
			nextLevelForCameraToReach = level;
			nextAmmoDropX = level.distanceBetweenAmmoDrops();
		} else {
			futureLevels.add(level);
		}
	}
	
	public Collection<Dino> getDinos() {
		return Collections.unmodifiableCollection(dinos);
	}
	
	public Collection<FloorBloodSplat> getBloodSplats() {
		return Collections.unmodifiableCollection(floorBloodSplats);
	}
	
	public Collection<MidAirBlood> getMidAirBloods() {
		return Collections.unmodifiableCollection(midAirBlood);
	}
	
	public Collection<Bullet> getBullets() {
		return Collections.unmodifiableCollection(bullets);
	}
	
	public Collection<AmmoPickup> getAmmoPickups() {
		return Collections.unmodifiableCollection(ammoPickups);
	}
	
	public Granny getGranny() {
		return granny;
	}
	
	public Vector2 getGunLocation() {
		return granny.getPosition().add(0, 0.75f);
	}
	
	public float getCheckpointX() {
		return checkpointX;
	}
	
	public void setCheckpointX(float x, boolean isSignpost) {
		this.checkpointX = x;
		this.checkpointIsSignpost = isSignpost;
	}
	
	public boolean checkpointIsSignpost() {
		return checkpointIsSignpost;
	}
	
	public float getFloorHeightAt(float x) {
		return stage.heightAt(x);
	}
	
	Stage getStage() {
		return stage;
	}
	
	Level getLevel() {
		return currentLevel;
	}
	
	Camera getCamera() {
		return camera;
	}
	
	public boolean gameEnded() {
		return granny.getHealth() <= 0;
	}
	
	public void update(float dt, WorldEvents events) {
		cleanupClipped();
		
		timeSinceLastShot += dt;
		
		if (currentLevel.shouldSpawnLeftDinos()) {
			timeSinceLastLeftDinoSpawn += dt;
		}
		
		if (currentLevel.shouldSpawnRightDinos()) {
			timeSinceLastRightDinoSpawn += dt;
		}
		
		// default to not aiming, let input set it to true
		if (!gunWasShot) {
			shootAtPos = null;
		}
		gunWasShot = false;
		
		if (scheduledBulletSpawnPos != null && scheduledBulletSpawnVelocity != null && timeSinceLastShot >= bulletSpawnRate) {
			bullets.add(createBullet(scheduledBulletSpawnPos, scheduledBulletSpawnVelocity, 10));
			scheduledBulletSpawnPos = null;
			scheduledBulletSpawnVelocity = null;
			timeSinceLastShot = 0;
			events.bulletIsShot();
		}
		
		if (currentLevel.shouldSpawnLeftDinos() && timeSinceLastLeftDinoSpawn >= currentLevel.getLeftDinosSpawnRate()) {
			timeSinceLastLeftDinoSpawn = 0;
			spawnLeftDino();
		}
		
		if (currentLevel.shouldSpawnRightDinos() && timeSinceLastRightDinoSpawn >= currentLevel.getRightDinosSpawnRate()) {
			timeSinceLastRightDinoSpawn = 0;
			spawnRightDino();
		}
		
		physics.update(dt, events);

		// keep track of how many dinos are on either side
		numLeftDinos = 0;
		numRightDinos = 0;
		for (Dino dino : dinos) {
			if (dino.getFacingDirection() == Facing.LEFT) {
				++numRightDinos;
			} else {
				++numLeftDinos;
			}
		}
		
		// AI
		for (Dino dino : dinos) {
			dino.navigate(this, dt);
		}
		granny.update(dt);
		
		camera.scrollTo(granny.getPosition());
		stage.keepUpWith(camera);
		
		// periodically spawn ammo
		if (camera.getPosition().x > nextAmmoDropX) {
			events.reachedAmmoDrop(nextAmmoDropX);
			nextAmmoDropX += currentLevel.distanceBetweenAmmoDrops();
		}
		
		// when the camera crosses the level boundary spawn the checkpoint and ammo-drop
		if (nextLevelForCameraToReach != null && camera.getPosition().x + camera.getWorldWidth() >= nextLevelForCameraToReach.getStartX()) {
			events.cameraReachedNextLevel(nextLevelForCameraToReach);
			nextLevelForGrannyToReach = nextLevelForCameraToReach;
			nextLevelForCameraToReach = null;
		}
		
		// when granny crosses the checkpoint it officially becomes the current level
		if (nextLevelForGrannyToReach != null && granny.getPosition().x >= getCheckpointX()) {
			currentLevel = nextLevelForGrannyToReach;
			nextLevelForGrannyToReach = null;
			
			events.grannyReachedCheckpoint(currentLevel);
			
			// schedule the next level
			nextLevelForCameraToReach = futureLevels.isEmpty() ? null : futureLevels.remove(0);
		}
		
		// continually generate terrain
		while (stage.reachedEnd()) {
			stage.appendSegment(currentLevel.getNextLevelSegment());
		}
	}
	
	// when stuff gets too far offscreen, clean it up
	private void cleanupClipped() {

		// uses lots of a regular for-loop to allow for concurrent modification
		
		for (int i = 0; i < midAirBlood.size(); ++i) {
			if (camera.isClipped(midAirBlood.get(i).getPosition())) {
				MidAirBlood b = midAirBlood.remove(i);
				physics.removeBody(b.getBody());
			}
		}
		
		for (int i = 0; i < floorBloodSplats.size(); ++i) {
			if (camera.isClipped(floorBloodSplats.get(i).getPosition())) {
				floorBloodSplats.remove(i);
			}
		}
		
		for (int i = 0; i < dinos.size(); ++i) {
			if (camera.isClipped(dinos.get(i).getPosition())) {
				removeDino(dinos.get(i));
			}
		}
		
		for (int i = 0; i < bullets.size(); ++i) {
			if (camera.isClipped(bullets.get(i).getPosition())) {
				Bullet b = bullets.remove(i);
				physics.removeBody(b.getBody());
			}
		}
	}

	private Granny createGranny(Vector2 position) {
		return new Granny(physics.createPlayer(position, 1, 1.2f), 100);
	}
	
	private Dino createDino(Dino.Kind kind, Vector2 position) {
		
		// sensible defaults
		float width = 1.5f;
		float height = 2.5f;
		float impulse = 1f;
		Vector2 jumpVector = new Vector2(5f, 25f);
		int damageInflicted = 10;
		int health = 100;
		
		if (kind == Dino.Kind.REX) {
			kind = Math.random() > 0.5 ? Dino.Kind.REX : Dino.Kind.ZOMBIE_REX;
			width = 1.5f;
			height = 2.5f;
			impulse = 0.4f;
			jumpVector = new Vector2(5f, 25f);
			damageInflicted = 20;
			health = 100;
		}
		else if (kind == Dino.Kind.DIPLO) {
			kind = Math.random() > 0.5 ? Dino.Kind.DIPLO : Dino.Kind.ZOMBIE_DIPLO;
			width = 3f;
			height = 3f;
			impulse = 0.8f;
			jumpVector = new Vector2(10f, 25f);
			damageInflicted = 15;
			health = 200;
		}
		else if (kind == Dino.Kind.TRITOPS) {
			kind = Math.random() > 0.5 ? Dino.Kind.TRITOPS : Dino.Kind.ZOMBIE_TRITOPS;
			width = 3f;
			height = 2f;
			impulse = 0.8f;
			jumpVector = new Vector2(15f, 40f);
			damageInflicted = 15;
			health = 100;
		}
		else if (kind == Dino.Kind.STEGO) {
			kind = Math.random() > 0.5 ? Dino.Kind.STEGO : Dino.Kind.ZOMBIE_STEGO;
			width = 3f;
			height = 2f;
			impulse = 0.5f;
			jumpVector = new Vector2(10f, 40f);
			damageInflicted = 40;
			health = 400;
		}
		
		return new Dino(kind, physics.createNPC(position, width, height), health, width, impulse, jumpVector, damageInflicted);
	}
	
	private Bullet createBullet(Vector2 position, Vector2 velocity, int damageInflicted) {
		return new Bullet(physics.createBullet(position, velocity, 0.1f), damageInflicted);
	}
	
	private MidAirBlood createMidAirBlood(Vector2 position, Vector2 velocity, boolean willSplat) {
		return new MidAirBlood(physics.createMidAirBlood(position, velocity, 0.1f), willSplat);
	}
	
	private AmmoPickup createAmmoPickup(Vector2 position, int numRounds) {
		return new AmmoPickup(physics.createPickup(position, 0.8f, 0.6f), numRounds);
	}
}
