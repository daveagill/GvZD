package week.of.awesome;

public class ScoreStats {

	private int distanceTravelled;
	private int killCount;
	
	private int initialScore;
	private boolean needToSetInitialScore;
	
	public ScoreStats() {
		reset();
	}
	
	public int getDistanceTravelled() { return distanceTravelled; }
	public int getKillCount() { return killCount; }
	public int getScore() {
		int score = getDistanceTravelled() * 3 + getKillCount() * 10;
		
		if (needToSetInitialScore) {
			needToSetInitialScore = false;
			initialScore = score;
		}
		
		return score - initialScore;
	}
	
	public void reset() {
		distanceTravelled = 0;
		killCount = 0;
		needToSetInitialScore = true;
	}
	
	public void setDistanceTravelled(int distance) {
		if (distance > distanceTravelled) {
			distanceTravelled = distance;
		}
	}
	
	public void setKillCount(int kills) {
		if (kills > killCount) {
			killCount = kills;
		}
	}
}
