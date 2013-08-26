package week.of.awesome;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

import week.of.awesome.Sensor.FloorSensor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Physics {
	// various categories for filtering
	private static short ALL = new Filter().maskBits;
	private static short FLOOR_CATEGORY = 0x2;
	private static short PLAYER_BLOCKER_CATEGORY = 0x4;
	private static short PLAYER_CATEGORY = 0x8;
	private static short NPC_CATEGORY = 0x10;
	private static short BULLET_CATEGORY = 0x20;
	private static short BLOOD_CATEGORY = 0x40;
	private static short PICKUP_CATEGORY = 0x80;
	
	private static float FLOOR_SENSOR_THICKNESS = 0.01f;
	
	
	private com.badlogic.gdx.physics.box2d.World b2dSim = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -10), true);
	private CollisionProcessor contactListener = new CollisionProcessor();
	
	private Collection<Body> bodiesToRemove = new HashSet<Body>();
	
	public Physics() {
		b2dSim.setContactListener(contactListener);
	}
	
	public void dispose() {
		removeDead();
		if (b2dSim.getBodyCount() > 0) {
			Logger.getGlobal().warning("Disposing of Box2d but there are still " + b2dSim.getBodyCount() + " bodies active.");
		}
		b2dSim.dispose();
	}
	
	public com.badlogic.gdx.physics.box2d.World getB2d() {
		return b2dSim;
	}

	public Body createPlayerBlocker(Vector2 position, float width, float height) {
		return createWorldBlock(position, width, height, PLAYER_BLOCKER_CATEGORY, PLAYER_CATEGORY);
	}
	
	public Body createFloor(Vector2 position, float width, float height) {
		return createWorldBlock(position, width, height, FLOOR_CATEGORY, ALL);
	}
	
	private Body createWorldBlock(Vector2 position, float width, float height, short category, short collidesWithCategories) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(position);
		bodyDef.fixedRotation = true;
		
		Body body = b2dSim.createBody(bodyDef);
		
		// origin is corner
		PolygonShape bodyShape = new PolygonShape();
		bodyShape.setAsBox(width/2, height/2, new Vector2(width/2, height/2), 0f);
		
		FixtureDef fixDef = new FixtureDef();
		fixDef.density = 1;
		fixDef.restitution = 0;
		fixDef.shape = bodyShape;
		fixDef.filter.categoryBits = category;
		fixDef.filter.maskBits = collidesWithCategories;
		
		body.createFixture(fixDef);
		
		bodyShape.dispose();
		
		return body;
	}
	
	public Body createPlayer(Vector2 position, float width, float height) {
		return createCapsule(position, width, height, PLAYER_CATEGORY, (short)(FLOOR_CATEGORY | PLAYER_BLOCKER_CATEGORY | NPC_CATEGORY | PICKUP_CATEGORY), 2f);
	}
	
	public Body createNPC(Vector2 position, float width, float height) {
		return createCapsule(position, width, height, NPC_CATEGORY, (short)(FLOOR_CATEGORY | BULLET_CATEGORY | PLAYER_CATEGORY), 0.5f);
	}
	
	private Body createCapsule(Vector2 position, float width, float height, short category, short collidesWithCategories, float friction) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.fixedRotation = true;
		
		Body body = b2dSim.createBody(bodyDef);
		
		FixtureDef fixDef = new FixtureDef();
		fixDef.density = 1;
		fixDef.restitution = 0.3f;
		fixDef.friction = friction;
		fixDef.filter.categoryBits = category;
		fixDef.filter.maskBits = collidesWithCategories;
		
		if (width == height) { // create a circle
			CircleShape circle = new CircleShape();
			circle.setRadius(width/2f);
			circle.setPosition(new Vector2(0, width/2f));
			
			fixDef.shape = circle;
			body.createFixture(fixDef);
			
			circle.dispose();
		}
		else { // create a capsule
			float capsuleRadius = Math.min(height, width)/2;
			
			CircleShape topCap = new CircleShape();
			topCap.setRadius(capsuleRadius);
			topCap.setPosition(new Vector2(0, height - capsuleRadius));
			
			CircleShape bottomCap = new CircleShape();
			bottomCap.setRadius(capsuleRadius);
			bottomCap.setPosition(new Vector2(0, capsuleRadius));
			
			// origin is in the character's middle, at their feet, some of the heights is taken by circular caps
			PolygonShape midTube = new PolygonShape();
			midTube.setAsBox(width/2, height/2-0.5f, new Vector2(0, height/2), 0f);
			
			
			// create main body fixtures
			fixDef.shape = midTube;
			body.createFixture(fixDef);
			
			fixDef.shape = topCap;
			body.createFixture(fixDef);

			fixDef.shape = bottomCap;
			body.createFixture(fixDef);
			
			// dispose shapes
			midTube.dispose();
			topCap.dispose();
			bottomCap.dispose();
		}
		
		
		// sensor is below the feet
		PolygonShape sensorShape = new PolygonShape();
		sensorShape.setAsBox(width/2.1f, FLOOR_SENSOR_THICKNESS, new Vector2(0, -FLOOR_SENSOR_THICKNESS), 0f);
		
		FixtureDef floorSensorDef = new FixtureDef();
		floorSensorDef.isSensor = true;
		floorSensorDef.shape = sensorShape;
		floorSensorDef.filter.maskBits = FLOOR_CATEGORY;
		
		Fixture floorSensorFix = body.createFixture(floorSensorDef);
		floorSensorFix.setUserData(new FloorSensor());
		
		sensorShape.dispose();
		
		return body;
	}
	
	public Body createBullet(Vector2 position, Vector2 velocity, float radius) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.fixedRotation = true;
		bodyDef.bullet = true;
		bodyDef.gravityScale = 0;
		bodyDef.linearVelocity.set(velocity);
		
		Body body = b2dSim.createBody(bodyDef);
		
		CircleShape bodyShape = new CircleShape();
		bodyShape.setRadius(radius);
		
		FixtureDef fixDef = new FixtureDef();
		fixDef.isSensor = true;
		fixDef.shape = bodyShape;
		fixDef.filter.categoryBits = BULLET_CATEGORY;
		
		body.createFixture(fixDef);
		
		bodyShape.dispose();
		
		return body;
	}
	
	public Body createMidAirBlood(Vector2 position, Vector2 velocity, float radius) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.fixedRotation = true;
		bodyDef.linearVelocity.set(velocity);
		
		Body body = b2dSim.createBody(bodyDef);
		
		CircleShape bodyShape = new CircleShape();
		bodyShape.setRadius(radius);
		
		FixtureDef fixDef = new FixtureDef();
		fixDef.isSensor = true;
		fixDef.shape = bodyShape;
		fixDef.filter.categoryBits = BLOOD_CATEGORY;
		fixDef.filter.maskBits = FLOOR_CATEGORY;
		
		body.createFixture(fixDef);
		
		bodyShape.dispose();
		
		return body;
	}
	
	public Body createPickup(Vector2 position, float width, float height) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.fixedRotation = true;
		
		Body body = b2dSim.createBody(bodyDef);
		
		// origin is bottom-center
		PolygonShape bodyShape = new PolygonShape();
		bodyShape.setAsBox(width/2, height/2, new Vector2(0, height/2), 0f);
		
		FixtureDef fixDef = new FixtureDef();
		fixDef.density = 1;
		fixDef.restitution = 0.3f;
		fixDef.friction = 2f;
		fixDef.shape = bodyShape;
		fixDef.filter.categoryBits = PICKUP_CATEGORY;
		fixDef.filter.maskBits = (short)(FLOOR_CATEGORY | PLAYER_CATEGORY);
		
		body.createFixture(fixDef);
		
		bodyShape.dispose();
		
		return body;
	}
	
	/** Doesn't actually remove the body immediately but schedules it for deletion,
	 * this allows the body to live for the current frame so that its positions etc can be
	 * more safely used for game logic.
	 * Also allows repeated deletion of the same body.
	 * @param body
	 */
	public void removeBody(Body body) {
		bodiesToRemove.add(body);
	}
	
	public void update(float dt, WorldEvents events) {
		removeDead();
		b2dSim.step(dt, 8, 3);
		contactListener.processCollisions(events);
	}
	
	private void removeDead() {
		// remove all dead bodies
		for (Body b : bodiesToRemove) {
			b2dSim.destroyBody(b);
		}
		bodiesToRemove.clear();
	}

}
