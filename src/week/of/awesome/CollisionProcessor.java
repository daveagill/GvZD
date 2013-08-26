package week.of.awesome;

import java.util.ArrayList;
import java.util.List;

import week.of.awesome.Sensor.FloorSensor;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionProcessor implements ContactListener {

	private List<Collision> collisionsBegun = new ArrayList<Collision>();
	private List<Collision> collisionsEnded = new ArrayList<Collision>();
	
	@Override
	public void beginContact(Contact contact) {
		Collision c = findCollisionEventForContact(contact);
		
		if (c != null) {
			collisionsBegun.add(c);
		}
	}

	@Override
	public void endContact(Contact contact) {
		Collision c = findCollisionEventForContact(contact);
		
		if (c != null) {
			collisionsEnded.add(c);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
	public void processCollisions(WorldEvents events) {
		for (Collision c : collisionsBegun) {
			c.triggerCollisionEvent(events);
		}
		
		for (Collision c : collisionsEnded) {
			c.triggerCollisionEndedEvent(events);
		}
		
		collisionsBegun.clear();
		collisionsEnded.clear();
	}
	
	private Collision findCollisionEventForContact(Contact contact) {
		Object objA = contact.getFixtureA().getBody().getUserData();
		Object objB = contact.getFixtureB().getBody().getUserData();
		Sensor sensorA = (Sensor)contact.getFixtureA().getUserData();
		Sensor sensorB = (Sensor)contact.getFixtureB().getUserData();
		
		if (objA == null || objB == null) { return null; }
		
		Collision c = lookupCollision(objA, objB, sensorA, sensorB);
		if (c == null) {
			c = lookupCollision(objB, objA, sensorB, sensorA);
		}
		return c;
	}
	
	private Collision lookupCollision(Object a, Object b, Sensor sensorA, Sensor sensorB) {
		if (a instanceof Granny && b instanceof Dino) {
			return new GrannyDinoCollision((Dino)b);
		}
		
		if (a instanceof Bullet && b instanceof Dino) {
			return new BulletDinoCollision((Bullet)a, (Dino)b);
		}
		
		if (sensorA instanceof FloorSensor && b instanceof Stage) {
			return new FloorSensorCollision(a, sensorA);
		}
		
		if (a instanceof MidAirBlood && b instanceof Stage) {
			return new MidAirBloodFloorCollision((MidAirBlood)a);
		}
		
		if (a instanceof Granny && b instanceof AmmoPickup) {
			return new GrannyAmmoPickupCollision((AmmoPickup)b);
		}
		
		return null;
	}
	
}
