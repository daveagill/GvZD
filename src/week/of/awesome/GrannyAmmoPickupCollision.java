package week.of.awesome;

public class GrannyAmmoPickupCollision extends Collision {

	private AmmoPickup ammo;
	
	public GrannyAmmoPickupCollision(AmmoPickup ammo) {
		this.ammo = ammo;
	}
	
	@Override
	public void triggerCollisionEvent(WorldEvents events) {
		events.pickupAmmo(ammo);
	}

}
