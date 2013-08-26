package week.of.awesome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {

	private List<LevelSegment> availableSegments = new ArrayList<LevelSegment>();
	private List<Dino.Kind> availableDinoTypes = new ArrayList<Dino.Kind>(); 
	
	private float startX = 0;
	private String signPostFilename;
	
	private float leftDinosSpawnRate = 5f;
	private float rightDinosSpawnRate = 5f;
	
	private float distanceBetweenAmmoDrops = 10;
	
	public Level(float startX) {
		this.startX = startX;
	}
	
	public void registerSegment(LevelSegment segment) {
		availableSegments.add(segment);
	}
	
	public void registerDinoType(Dino.Kind kind, int freq) {
		for (int i = 0; i < freq; ++i) {
			availableDinoTypes.add(kind);
		}
	}
	
	public float getStartX() {
		return startX;
	}
	
	public void setSecondsPerLeftDinoSpawn(int num) {
		leftDinosSpawnRate = (float)num;
	}
	
	
	public void setSecondsPerRightDinoSpawn(int num) {
		rightDinosSpawnRate = (float)num;
	}
	
	public boolean shouldSpawnLeftDinos() {
		return getLeftDinosSpawnRate() > 0;
	}
	
	public boolean shouldSpawnRightDinos() {
		return getRightDinosSpawnRate() > 0;
	}
	
	public float getLeftDinosSpawnRate() {
		return leftDinosSpawnRate;
	}
	
	public float getRightDinosSpawnRate() {
		return rightDinosSpawnRate;
	}
	
	public void setSignPostFilename(String filename) {
		this.signPostFilename = filename;
	}
	
	public String getSignPostFilename() {
		return signPostFilename;
	}
	
	public LevelSegment getNextLevelSegment() {
		return availableSegments.get( new Random().nextInt(availableSegments.size()));
	}
	
	public Dino.Kind getDinoKind() {
		return availableDinoTypes.get( new Random().nextInt(availableDinoTypes.size()));
	}
	
	public float distanceBetweenAmmoDrops() {
		return distanceBetweenAmmoDrops;
	}
	
	public void setDistanceBetweenAmmoDrops(float distance) {
		this.distanceBetweenAmmoDrops = distance;
	}
}
