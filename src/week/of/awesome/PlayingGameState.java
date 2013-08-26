package week.of.awesome;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Input;

public class PlayingGameState implements GameState {

	private ScoreStats scoring;
	
	private Camera camera;
	private World world;
	private WorldEvents events;
	private InputMapper input;
	private SoundEffects sounds;
	private GameplayRenderer gameplayRenderer;
	
	private GameState gameOverGameState;
	private ReadingSignpostGameState readingSignpostGameState;
	private boolean isReadingSignpost = false;
	
	public PlayingGameState(Assets assets, Renderer renderer, Input gdxInput, Audio gdxAudio, ScoreStats scoring) {
		camera = new Camera(renderer.getScreenWidth(), renderer.getScreenHeight());
		sounds = new SoundEffects(assets, gdxAudio);
		events = new WorldEvents(this, sounds);
		input = new InputMapper(gdxInput, events, camera);
		gameplayRenderer = new GameplayRenderer(renderer, assets);
		
		readingSignpostGameState = new ReadingSignpostGameState(assets, renderer, gdxInput);
		readingSignpostGameState.setBackgroundGameState(this);
		
		this.scoring = scoring;
	}
	
	public void dispose() {
		gameplayRenderer.dispose();
		readingSignpostGameState.dispose();
		sounds.dispose();
	}
	
	public void setGameOverGameState(GameState gameover) {
		this.gameOverGameState = gameover;
	}

	@Override
	public void enter(GameState previousGameState) {
		scoring.reset();
		world = new World(camera);
		events.resetWorld(world);
		
		WorldSetup.populate(world);
		updateScoring();
	}

	@Override
	public void leave() {
		world.dispose();
		world = null;
	}

	@Override
	public GameState update(float dt) {
		if (isReadingSignpost) {
			if (readingSignpostGameState.update(dt) == this) {
				isReadingSignpost = false;
				readingSignpostGameState.leave();
			}
			return this;
		}
		
		input.process();
		world.update(dt, events);
		
		updateScoring();
		
		return world.gameEnded() ? gameOverGameState : this;
	}

	@Override
	public void render() {
		gameplayRenderer.draw(world, scoring);
		if (isReadingSignpost) {
			readingSignpostGameState.render();
		}
	}
	
	public void beginReadingSignpost() {
		isReadingSignpost = true;
		readingSignpostGameState.setSignpostScreenForLevel(world.getLevel());
		readingSignpostGameState.enter(null);
	}
	
	public void debugRender() {
		gameplayRenderer.drawPhysicsDebugging(world);
	}
	
	private void updateScoring() {
		scoring.setDistanceTravelled(world.getGranny().getDistanceTravelled());
		scoring.setKillCount(world.getGranny().getKillCount());
	}
}
