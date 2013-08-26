package week.of.awesome;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.TimeUtils;

public class ApplicationEventListener implements ApplicationListener {

	private static long NANOS_IN_A_SECOND = 1000000000;
	private static long fixedTimestepNanos = (long)(1f/60f * NANOS_IN_A_SECOND);
	
	private Assets assets;
	private Renderer renderer;
	private Box2DDebugRenderer debugRenderer;
	
	private Music backgroundMusic;
	
	private ScoreStats scoring;
	private Highscores highscores;
	
	private StartScreenGameState startScreenGameState;
	private IntroSceneGameState introSceneGameState;
	private PlayingGameState playingGameState;
	private GameOverScreenGameState gameOverGameState;
	private HighscoresGameState highscoresGameState;
	private GameState currentGameState;
	
	private long lastFrameTimeNanos;
	private long accumulatedTimeNanos;
	
	@Override
	public void create() {
		assets = new Assets();
		renderer = new Renderer(Gdx.graphics, assets);
		debugRenderer = new Box2DDebugRenderer();
		
		backgroundMusic = Gdx.audio.newMusic(assets.getMusic("In a Heartbeat.mp3"));
		backgroundMusic.setLooping(true);
		backgroundMusic.play();
		
		scoring = new ScoreStats();
		highscores = new Highscores(assets.getHighScoreXML());
		
		startScreenGameState = new StartScreenGameState(assets, renderer, Gdx.input);
		introSceneGameState = new IntroSceneGameState(assets, renderer, Gdx.input);
		playingGameState = new PlayingGameState(assets, renderer, Gdx.input, Gdx.audio, scoring);
		gameOverGameState = new GameOverScreenGameState(assets, renderer, Gdx.input, scoring, highscores);
		highscoresGameState = new HighscoresGameState(assets, renderer, Gdx.input, highscores);
		
		// wire up the transitions between gamestates
		startScreenGameState.setNextGameState(introSceneGameState);
		startScreenGameState.setShowHighscoresGameState(highscoresGameState);
		introSceneGameState.setNextGameState(playingGameState);
		playingGameState.setGameOverGameState(gameOverGameState);
		gameOverGameState.setRestartGameState(playingGameState);
		gameOverGameState.setShowHighscoresGameState(highscoresGameState);
		highscoresGameState.addNextGameState(startScreenGameState, startScreenGameState);
		highscoresGameState.addNextGameState(gameOverGameState, playingGameState);
		
		currentGameState = playingGameState;//startScreenGameState;
		currentGameState.enter(null);
		
		lastFrameTimeNanos = getTime();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		// calculate frametime delta
		long time = getTime();
		long deltaNanos = time - lastFrameTimeNanos;
		lastFrameTimeNanos = time;
		
		accumulatedTimeNanos += deltaNanos;
		
		while (accumulatedTimeNanos >= fixedTimestepNanos) {
			GameState nextGameState =
					currentGameState.update((float)fixedTimestepNanos / NANOS_IN_A_SECOND);
			
			if (nextGameState != null && nextGameState != currentGameState) {
				currentGameState.leave();
				nextGameState.enter(currentGameState);
				currentGameState = nextGameState;
			}
			
			accumulatedTimeNanos -= fixedTimestepNanos;
		}
		
		renderer.begin();
		currentGameState.render();
		renderer.end();
		
		if (currentGameState == playingGameState) {
			playingGameState.debugRender();
		}
	}

	@Override
	public void pause() {
		// do nothing
	}

	@Override
	public void resume() {
		// do nothing
	}

	@Override
	public void dispose() {
		currentGameState.leave();
		playingGameState.dispose();
		gameOverGameState.dispose();
		renderer.dispose();
		debugRenderer.dispose();
		backgroundMusic.dispose();
	}
	
	private static long getTime() {
		return TimeUtils.nanoTime();
	}

}
