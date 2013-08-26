package week.of.awesome;

public interface GameState {

	public void enter(GameState previousGameState);
	public void leave();
	
	public GameState update(float dt);
	public void render();
	
}
