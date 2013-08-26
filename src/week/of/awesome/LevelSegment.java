package week.of.awesome;

public class LevelSegment {

	private int[] heightMap;
	
	public LevelSegment(int... heights) {
		heightMap = heights.clone();
	}
	
	public int length() {
		return heightMap.length;
	}
	
	public int startHeight() {
		return heightMap[0];
	}
	
	public int endHeight() {
		return heightMap[heightMap.length-1];
	}
	
	public int[] getHeightMap() {
		return heightMap.clone();
	}
}
