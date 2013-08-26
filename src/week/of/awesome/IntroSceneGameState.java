package week.of.awesome;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input;

public class IntroSceneGameState extends AbstractMenuScreenGameState {

	private static float HALF_TRANSITION_DURATION = 1f;
	private static enum FrameTransition { FADE_TO_BLACK, NONE }
	
	private static class SceneFrame {
		public String filename;
		public float duration;
		public FrameTransition transition;
		
		public SceneFrame(String filename, float duration, FrameTransition transition) {
			this.filename = filename;
			this.duration = duration;
			this.transition = transition;
		}
	}
	
	private List<SceneFrame> frames = new ArrayList<SceneFrame>();
	private int currentFrame;
	private float timeLeftOnFrame;
	private boolean doingFadeOut = false;
	private boolean doingFadeIn = false;
	
	private GameState nextState;
	
	public IntroSceneGameState(Assets assets, Renderer renderer, Input gdxInput) {
		super(assets, renderer, gdxInput);
		frames.add(new SceneFrame("story-1.png", 2f, FrameTransition.FADE_TO_BLACK));
		frames.add(new SceneFrame("story-2.png", 2f, FrameTransition.FADE_TO_BLACK));
		frames.add(new SceneFrame("story-3.png", 2f, FrameTransition.FADE_TO_BLACK));
		frames.add(new SceneFrame("story-4.png", 2f, FrameTransition.FADE_TO_BLACK));
		frames.add(new SceneFrame("story-5-1.png", 3f, FrameTransition.NONE));
		frames.add(new SceneFrame("story-5-2.png", 2f, FrameTransition.NONE));
		frames.add(new SceneFrame("story-5-3.png", 2f, FrameTransition.FADE_TO_BLACK));
		frames.add(new SceneFrame("story-6-1.png", 3f, FrameTransition.NONE));
		frames.add(new SceneFrame("story-6-2.png", 3f, FrameTransition.NONE));
		frames.add(new SceneFrame("story-6-3.png", 3f, FrameTransition.NONE));
		frames.add(new SceneFrame("story-6-4.png", 2f, FrameTransition.FADE_TO_BLACK));
	}
	
	public void setNextGameState(GameState nextState) {
		this.nextState = nextState;
	}

	@Override
	protected void enterScreen(GameState previousGameState) {
		doingFadeOut = false;
		doingFadeIn = false;
		currentFrame = 0;
		timeLeftOnFrame += frames.get(currentFrame).duration;
		super.setAlpha(1f);
		loadFrame();
	}

	@Override
	protected void leaveScreen() {
	}

	@Override
	protected void disposeScreen() {
	}

	@Override
	public GameState update(float dt) {
		timeLeftOnFrame -= dt;
		
		SceneFrame frame = frames.get(currentFrame);
		
		if (timeLeftOnFrame <= 0) {
			
			if (!doingFadeOut && !doingFadeIn && frame.transition == FrameTransition.FADE_TO_BLACK) {
				doingFadeOut = true;
				timeLeftOnFrame += HALF_TRANSITION_DURATION;
			}
			else if (doingFadeOut) { // switch from fade-out to fade-in
				doingFadeOut = false;
				doingFadeIn = true;
				timeLeftOnFrame += HALF_TRANSITION_DURATION;
			}
			else { // must have finished fading in or not doing a fade at all, fully onto the next frame now
				doingFadeIn = false;
				timeLeftOnFrame += frames.get(currentFrame).duration;		
			}
			
			// if we're fading in, or popping in, then we need to advance the frame
			if (doingFadeIn || frame.transition == FrameTransition.NONE) {
				++currentFrame;
				
				// if we've run out of frames then just move to the next game-state
				if (currentFrame == frames.size()) {
					return nextState;
				}
				
				loadFrame();
			}
		}
		else if (doingFadeOut) {
			super.setAlpha(timeLeftOnFrame / HALF_TRANSITION_DURATION);	
		}
		else if (doingFadeIn) {
			super.setAlpha(1f - timeLeftOnFrame / HALF_TRANSITION_DURATION);	
		}
		
		return this;
	}

	@Override
	protected void renderUI() {
	}

	@Override
	protected boolean keyTyped(char character, int keycode) {
		return false;
	}

	@Override
	protected boolean mouseClicked() {
		return false;
	}

	private void loadFrame() {
		super.loadScreen("intro-scene/" + frames.get(currentFrame).filename);
	}
}
