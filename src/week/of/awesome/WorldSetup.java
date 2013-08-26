package week.of.awesome;

import com.badlogic.gdx.math.Vector2;

public class WorldSetup {

	public static void populate(World world) {
		//world.spawnDino();
		world.respawnGranny();
		
		LevelSegment seg0 = new LevelSegment(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		LevelSegment seg1 = new LevelSegment(1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1);
		LevelSegment seg2 = new LevelSegment(1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1);
		LevelSegment seg3 = new LevelSegment(1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 3, 3, 3, 3, 3, 2, 2, 1, 1, 1, 1, 1, 1);
		
		Level level1 = new Level(0);
		level1.setSignPostFilename("sign-1.png");
		level1.registerSegment(seg0);
		level1.registerDinoType(Dino.Kind.ZOMBIE_REX, 1);
		level1.registerDinoType(Dino.Kind.REX, 1);
		level1.setSecondsPerLeftDinoSpawn(0);
		level1.setSecondsPerRightDinoSpawn(8);
		level1.setDistanceBetweenAmmoDrops(10);
		
		Level level2 = new Level(50);
		level2.setSignPostFilename("sign-2.png");
		level2.registerSegment(seg1);
		level2.registerSegment(seg2);
		level2.registerDinoType(Dino.Kind.ZOMBIE_REX, 1);
		level2.registerDinoType(Dino.Kind.REX, 1);
		level2.registerDinoType(Dino.Kind.DIPLO, 1);
		level2.setSecondsPerLeftDinoSpawn(15);
		level2.setSecondsPerRightDinoSpawn(5);
		level2.setDistanceBetweenAmmoDrops(40);
		
		Level level3 = new Level(150);
		level3.setSignPostFilename("sign-3.png");
		level3.registerSegment(seg1);
		level3.registerSegment(seg2);
		level3.registerSegment(seg3);
		level3.registerDinoType(Dino.Kind.ZOMBIE_REX, 1);
		level3.registerDinoType(Dino.Kind.REX, 1);
		level3.registerDinoType(Dino.Kind.DIPLO, 1);
		level3.registerDinoType(Dino.Kind.TRITOPS, 5);
		level3.setSecondsPerLeftDinoSpawn(15);
		level3.setSecondsPerRightDinoSpawn(5);
		level3.setDistanceBetweenAmmoDrops(40);
		
		Level level4 = new Level(200);
		level4.setSignPostFilename("sign-4.png");
		level4.registerSegment(seg1);
		level4.registerSegment(seg2);
		level4.registerDinoType(Dino.Kind.ZOMBIE_REX, 1);
		level4.registerDinoType(Dino.Kind.REX, 1);
		level4.registerDinoType(Dino.Kind.DIPLO, 1);
		level4.registerDinoType(Dino.Kind.STEGO, 5);
		level4.setSecondsPerLeftDinoSpawn(15);
		level4.setSecondsPerRightDinoSpawn(5);
		level4.setDistanceBetweenAmmoDrops(40);
		
		// level 5 is just to switched to balanced dinos
		Level level5 = new Level(250);
		level5.registerSegment(seg1);
		level5.registerSegment(seg2);
		level5.registerSegment(seg3);
		level5.registerDinoType(Dino.Kind.REX, 1);
		level5.registerDinoType(Dino.Kind.ZOMBIE_REX, 1);
		level5.registerDinoType(Dino.Kind.DIPLO, 1);
		level5.registerDinoType(Dino.Kind.TRITOPS, 1);
		level5.registerDinoType(Dino.Kind.STEGO, 1);
		level5.setSecondsPerLeftDinoSpawn(10);
		level5.setSecondsPerRightDinoSpawn(5);
		level5.setDistanceBetweenAmmoDrops(40);
		
		Level level6 = new Level(350);
		level6.setSignPostFilename("sign-5.png");
		level6.registerSegment(seg1);
		level6.registerSegment(seg2);
		level6.registerSegment(seg3);
		level6.registerDinoType(Dino.Kind.REX, 1);
		level6.registerDinoType(Dino.Kind.DIPLO, 1);
		level6.registerDinoType(Dino.Kind.TRITOPS, 1);
		level6.registerDinoType(Dino.Kind.STEGO, 1);
		level6.setSecondsPerLeftDinoSpawn(10);
		level6.setSecondsPerRightDinoSpawn(5);
		level6.setDistanceBetweenAmmoDrops(40);
		
		Level level7 = new Level(500);
		level7.setSignPostFilename("sign-6.png");
		level7.registerSegment(seg1);
		level7.registerSegment(seg2);
		level7.registerSegment(seg3);
		level7.registerDinoType(Dino.Kind.REX, 1);
		level7.registerDinoType(Dino.Kind.DIPLO, 1);
		level7.registerDinoType(Dino.Kind.TRITOPS, 1);
		level7.registerDinoType(Dino.Kind.STEGO, 1);
		level7.setSecondsPerLeftDinoSpawn(8);
		level7.setSecondsPerRightDinoSpawn(5);
		level7.setDistanceBetweenAmmoDrops(60);
		
		world.registerLevel(level1);
		world.registerLevel(level2);
		world.registerLevel(level3);
		world.registerLevel(level4);
		world.registerLevel(level5);
		world.registerLevel(level6);
		world.registerLevel(level7);
		
		world.setAmmoRemaining(50);
	}

}
