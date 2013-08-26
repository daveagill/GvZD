package week.of.awesome;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class GameplayRenderer {

	private static float foregroundYOffset = 0.1f;
	
	private Renderer renderer;
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	private Vector2 healthTextPos;
	private Vector2 ammoTextPos;
	private Vector2 distanceTextPos;
	private Vector2 killCountTextPos;
	private Vector2 scoreTextPos;
	
	private Texture crosshairTex;
	private Texture bloodSplatTex;
	private Texture midAirBloodTex;
	private Texture floorTex;
	private Texture bulletTex;
	
	private Texture ammoPickupTex;
	private Texture signpostTex;
	
	private Texture grannyTex;
	private Texture armlessGrannyTex;
	private Texture grannyLeftLegTex;
	private Texture grannyRightLegTex;
	private Texture grannyArmTex;
	
	private Texture sceneryTex;
	
	private Sprite crosshairSpr;
	private Sprite grannySpr;
	private Sprite armlessGrannySpr;
	private Sprite bloodSplatSpr;
	private Sprite midAirBloodSpr;
	private Sprite floorSpr;
	private Sprite bulletSpr;
	private Sprite ammoPickupSpr;
	private Sprite signpostSpr;
	private Map<Dino.Kind, DinoSprites> dinoSprites = new HashMap<Dino.Kind, DinoSprites>();
	private Sprite grannyLeftLegSpr;
	private Sprite grannyRightLegSpr;
	private Sprite grannyArmSpr;
	private Sprite scenerySpr;
	
	public GameplayRenderer(Renderer renderer, Assets assets) {
		this.renderer = renderer;
		
		healthTextPos = new Vector2(0, renderer.getTextLineHeightFromTop(0));
		ammoTextPos = new Vector2(0, renderer.getTextLineHeightFromTop(1));
		
		scoreTextPos = new Vector2(renderer.getScreenWidth(), renderer.getTextLineHeightFromTop(0));
		distanceTextPos = new Vector2(renderer.getScreenWidth(), renderer.getTextLineHeightFromTop(1));
		killCountTextPos = new Vector2(renderer.getScreenWidth(), renderer.getTextLineHeightFromTop(2));

		crosshairTex = new Texture(assets.getSprite("crosshair.png"));
		midAirBloodTex = new Texture(assets.getSprite("midairblood.png"));
		bloodSplatTex = new Texture(assets.getSprite("bloodsplat.png"));
		floorTex = new Texture(assets.getSprite("floor.png"));
		bulletTex = new Texture(assets.getSprite("bullet.png"));
		
		ammoPickupTex = new Texture(assets.getSprite("ammoPickup.png"));
		signpostTex = new Texture(assets.getSprite("signpost.png"));
		
		grannyTex = new Texture(assets.getSprite("granny.png"));
		armlessGrannyTex = new Texture(assets.getSprite("armless-granny.png"));
		grannyRightLegTex = new Texture(assets.getSprite("granny-right-leg.png"));
		grannyLeftLegTex = new Texture(assets.getSprite("granny-left-leg.png"));
		grannyArmTex = new Texture(assets.getSprite("granny-arm.png"));
		
		sceneryTex = new Texture(assets.getSprite("scenery.png"));
	
		grannySpr = Renderer.sprite(grannyTex, 125, 148, 0.7f);
		armlessGrannySpr = Renderer.sprite(armlessGrannyTex, 125, 148, 0.7f);
		
		crosshairSpr = Renderer.sprite(crosshairTex, crosshairTex.getWidth(), crosshairTex.getHeight(), 1f);
		midAirBloodSpr = Renderer.sprite(midAirBloodTex, midAirBloodTex.getWidth(), midAirBloodTex.getHeight(), 1f);
		bloodSplatSpr = Renderer.sprite(bloodSplatTex, bloodSplatTex.getWidth(), bloodSplatTex.getHeight(), 1f);//279, 159, 0.5f);
		Renderer.setOrigin(bloodSplatSpr, 0, 0);
		floorSpr = Renderer.sprite(floorTex, floorTex.getWidth(), floorTex.getHeight(), Camera.wtos(Stage.BLOCK_SIZE) / floorTex.getWidth());
		bulletSpr = Renderer.sprite(bulletTex, 32, 32, 0.7f);
		ammoPickupSpr = Renderer.sprite(ammoPickupTex, 190, 142, 0.4f);
		signpostSpr = Renderer.sprite(signpostTex, signpostTex.getWidth(), signpostTex.getHeight(), 1);
		
		DinoSprites rexSprs = new DinoSprites(assets, 0.5f);
		rexSprs.setBody("rex.png", 416, 362);
		rexSprs.setLeftLeg("dino-left-leg.png", 90, 90);
		rexSprs.setRightLeg("dino-right-leg.png", 90, 90);
		
		DinoSprites zombieRexSprs = new DinoSprites(assets, 0.5f);
		zombieRexSprs.setBody("rex2.png", 416, 362);
		zombieRexSprs.setLeftLeg("dino-left-leg.png", 90, 90);
		zombieRexSprs.setRightLeg("dino-right-leg.png", 90, 90);
		
		DinoSprites diploSprs = new DinoSprites(assets, 0.8f);
		diploSprs.setBody("diplo.png", 416, 362);
		diploSprs.setLeftLeg("dino-left-leg.png", 90, 90);
		diploSprs.setRightLeg("dino-right-leg.png", 90, 90);
		
		DinoSprites zombiediploSprs = new DinoSprites(assets, 0.8f);
		zombiediploSprs.setBody("diplo2.png", 416, 362);
		zombiediploSprs.setLeftLeg("dino-left-leg.png", 90, 90);
		zombiediploSprs.setRightLeg("dino-right-leg.png", 90, 90);
		
		DinoSprites tritopsSprs = new DinoSprites(assets, 0.5f);
		tritopsSprs.setBody("tritops.png", 416, 362);
		tritopsSprs.setLeftLeg("dino-left-leg.png", 90, 90);
		tritopsSprs.setRightLeg("dino-right-leg.png", 90, 90);
		
		DinoSprites zombietritopsSprs = new DinoSprites(assets, 0.5f);
		zombietritopsSprs.setBody("tritops2.png", 416, 362);
		zombietritopsSprs.setLeftLeg("dino-left-leg.png", 90, 90);
		zombietritopsSprs.setRightLeg("dino-right-leg.png", 90, 90);
		
		DinoSprites stegoSprs = new DinoSprites(assets, 0.5f);
		stegoSprs.setBody("stego.png", 416, 362);
		stegoSprs.setLeftLeg("dino-left-leg.png", 90, 90);
		stegoSprs.setRightLeg("dino-right-leg.png", 90, 90);
		
		DinoSprites zombiestegoSprs = new DinoSprites(assets, 0.5f);
		zombiestegoSprs.setBody("stego2.png", 416, 362);
		zombiestegoSprs.setLeftLeg("dino-left-leg.png", 90, 90);
		zombiestegoSprs.setRightLeg("dino-right-leg.png", 90, 90);
		
		dinoSprites.put(Dino.Kind.REX, rexSprs);
		dinoSprites.put(Dino.Kind.ZOMBIE_REX, zombieRexSprs);
		dinoSprites.put(Dino.Kind.DIPLO, diploSprs);
		dinoSprites.put(Dino.Kind.ZOMBIE_DIPLO, zombiediploSprs);
		dinoSprites.put(Dino.Kind.TRITOPS, tritopsSprs);
		dinoSprites.put(Dino.Kind.STEGO, stegoSprs);
		dinoSprites.put(Dino.Kind.ZOMBIE_TRITOPS, zombietritopsSprs);
		dinoSprites.put(Dino.Kind.ZOMBIE_STEGO, zombiestegoSprs);
		
		
		grannyRightLegSpr = Renderer.sprite(grannyRightLegTex, 24, 26, 0.7f);
		Renderer.setOrigin(grannyRightLegSpr, 0, 1);
		
		grannyLeftLegSpr = Renderer.sprite(grannyLeftLegTex, 24, 26, 0.7f);
		Renderer.setOrigin(grannyLeftLegSpr, 0, 1);
		
		grannyArmSpr = Renderer.sprite(grannyArmTex, grannyArmTex.getWidth(), grannyArmTex.getHeight(), 0.7f);
		Renderer.setOrigin(grannyArmSpr, -1, 0);
		
		// can't use Renderer.sprite because we want a screen-space image
		scenerySpr = new Sprite(sceneryTex, sceneryTex.getWidth(), renderer.getScreenHeight());
		scenerySpr.setOrigin(0, 0);
	}
	
	public void dispose() {
		grannyTex.dispose();
		bloodSplatTex.dispose();
		floorTex.dispose();
		bulletTex.dispose();
		
		for (DinoSprites bodyParts : dinoSprites.values()) {
			bodyParts.dispose();
		}
		
		debugRenderer.dispose();
	}
	
	public void draw(World world, ScoreStats scoring) {
		
		// draw the background scenery
		renderer.setTransformMatrix(new Matrix4());
		renderer.drawAtWithAngle(scenerySpr, new Vector2(0, 0), 0);
		
		// draw the stats, dialogs, etc
		renderer.setTransformMatrix(new Matrix4());
		drawUI(world, scoring);

		// draw the game-world itself
		Matrix4 transform = new Matrix4();
		transform.scale(Camera.SCREEN_UNITS_PER_WORLD_UNIT, Camera.SCREEN_UNITS_PER_WORLD_UNIT, 1);
		transform.translate(-world.getCamera().getPosition().x, -world.getCamera().getPosition().y, 0);
		
		renderer.setTransformMatrix(transform);
		drawWorld(world);
	}
	
	private void drawUI(World world, ScoreStats scoring) {
		String healthText = "Health: " + world.getGranny().getHealth();
		String ammoText = "Ammo: " + world.getAmmoRemaining();
		String distanceText = "Distance Travelled: " + scoring.getDistanceTravelled() + "m";
		String killsText = "Kills: " + scoring.getKillCount();
		String scoreText = "Score: " + scoring.getScore();

		renderer.drawLeftAlignedText(healthText, healthTextPos);
		renderer.drawLeftAlignedText(ammoText, ammoTextPos);
		//renderer.drawRightAlignedText(distanceText, distanceTextPos);
		//renderer.drawRightAlignedText(killsText, killCountTextPos);
		renderer.drawRightAlignedText(scoreText , scoreTextPos);
	}
	
	private void drawWorld(World world) {
		// n.b. Z-ordering is decided by painter's algorithm
		
		// draw the terrain
		Stage stage = world.getStage();
		for (int i = 0; i < stage.getHeightMap().size(); ++i) {
			int height = stage.getHeightMap().get(i);
			renderer.drawAtWithHeight(floorSpr, new Vector2(stage.getLeftPosition() + i * Stage.BLOCK_SIZE, 0), height);
		}
		
		// draw blood-splats
		for (FloorBloodSplat blood : world.getBloodSplats()) {
			bloodSplatSpr.setRotation(blood.getRotation());
			renderer.drawAtWithAngle(bloodSplatSpr, blood.getPosition().sub(0, Stage.BLOCK_SIZE/4f), blood.getRotation());
		}
		
		// draw the signpost
		if (world.checkpointIsSignpost()) {
			float signpostX = world.getCheckpointX();
			float signpostY = world.getFloorHeightAt(signpostX);
			renderer.drawCenterBottom(signpostSpr, new Vector2(signpostX, signpostY - foregroundYOffset), false);
		}
		
		// draw pickups
		for (AmmoPickup ammo : world.getAmmoPickups()) {
			renderer.drawCenterBottom(ammoPickupSpr, ammo.getPosition().cpy().sub(0, foregroundYOffset), false);
		}
	
		// draw dinosaurs
		for (Dino dino : world.getDinos()) {
			DinoSprites bodyParts = dinoSprites.get(dino.getKind());
			Sprite bodySpr = bodyParts.getBodySprite();
			Sprite leftLegSpr = bodyParts.getLeftLegSprite();
			Sprite rightLegSpr = bodyParts.getRightLegSprite();
			
			boolean mirrorX = dino.getFacingDirection() == Facing.LEFT;
			
			DinoBodyPartAnimator dinoAnim = new DinoBodyPartAnimator(dino);
			renderer.drawAtScreenOffsetWithAngle(rightLegSpr, dino.getPosition(), dinoAnim.getRightLegPosition().cpy().mul(bodyParts.getScale(), bodyParts.getScale()).sub(0, foregroundYOffset), dinoAnim.getRightLegAngle(), mirrorX, false);
			renderer.drawAtScreenOffsetWithAngle(leftLegSpr, dino.getPosition(), dinoAnim.getLeftLegPosition().cpy().mul(bodyParts.getScale(), bodyParts.getScale()).sub(0, foregroundYOffset), dinoAnim.getLeftLegAngle(), mirrorX, false);
			renderer.drawCenterBottom(bodySpr, dino.getPosition().sub(0, foregroundYOffset), mirrorX);
		}
		
		// draw mid-air blood
		for (MidAirBlood blood : world.getMidAirBloods()) {
			renderer.drawCenteredAt(midAirBloodSpr, blood.getPosition().sub(0, foregroundYOffset));
		}
		
		// draw bullets
		for (Bullet bullet : world.getBullets()) {
			renderer.drawCenteredAt(bulletSpr, bullet.getPosition().sub(0, foregroundYOffset));
		}
		
		// draw granny
		Granny granny = world.getGranny();
		boolean mirrorGrannyX = granny.getFacingDirection() == Facing.LEFT;
		GrannyBodyPartAnimator grannyAnim = new GrannyBodyPartAnimator(granny, world.getGunLocation(), world.getShootAtPosition());
		
		if (world.getShootAtPosition() != null) {
			renderer.drawCenterBottom(armlessGrannySpr, granny.getPosition().sub(0, foregroundYOffset), mirrorGrannyX);
			renderer.drawAtScreenOffsetWithAngle(grannyArmSpr, world.getGunLocation().sub(0, foregroundYOffset), new Vector2(0, 0), grannyAnim.getArmAngle(), false, mirrorGrannyX);
		} else {
			renderer.drawCenterBottom(grannySpr, granny.getPosition().sub(0, foregroundYOffset), mirrorGrannyX);
		}
		
		renderer.drawAtScreenOffsetWithAngle(grannyRightLegSpr, granny.getPosition().sub(0, foregroundYOffset), grannyAnim.getRightLegPosition(), 0, mirrorGrannyX, false);
		renderer.drawAtScreenOffsetWithAngle(grannyLeftLegSpr, granny.getPosition().sub(0, foregroundYOffset), grannyAnim.getLeftLegPosition(), 0, mirrorGrannyX, false);

		// draw crosshair
		renderer.drawCenteredAt(crosshairSpr, world.getAimAtPosition());
	}
	
	public void drawPhysicsDebugging(World world) {
		//debugRenderer.render(world.getB2d(), renderer.getCombinedMatrix());
	}
}
