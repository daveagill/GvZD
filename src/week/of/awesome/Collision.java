package week.of.awesome;

import com.badlogic.gdx.physics.box2d.Contact;

public abstract class Collision {

	public void onBeginContact(Contact contact) { }
	public void onEndContact(Contact contact) { }
	public abstract void triggerCollisionEvent(WorldEvents events);
	public void triggerCollisionEndedEvent(WorldEvents events) { }

}
