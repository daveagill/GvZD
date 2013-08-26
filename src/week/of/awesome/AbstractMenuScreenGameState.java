package week.of.awesome;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

public abstract class AbstractMenuScreenGameState implements GameState {

	private Assets assets;
	protected Renderer renderer;
	private Texture screenTex;
	private TextureRegion screen;
	private float alpha = 1f;
	
	private Input gdxInput;
	
	public AbstractMenuScreenGameState(Assets assets, Renderer renderer, Input gdxInput) {
		this.assets = assets;
		this.renderer = renderer;
		this.gdxInput = gdxInput;
	}
	
	protected void loadScreen(String screenFilename, int srcWidth, int srcHeight) {
		if (screenTex != null) {
			screenTex.dispose();
		}
		this.screenTex = new Texture(assets.getScreen(screenFilename));
		this.screen = new TextureRegion(screenTex, 0, 0, srcWidth, srcHeight);
	}
	
	protected void loadScreen(String screenFilename) {
		loadScreen(screenFilename, renderer.getScreenWidth(), renderer.getScreenHeight());
	}
	
	protected void setAlpha(float alpha) {
		if (alpha < 0) { alpha = 0; }
		if (alpha > 1) { alpha = 1; }
		this.alpha = alpha;
	}
	
	public final void dispose() {
		if (screenTex != null) {
			screenTex.dispose();
		}
		disposeScreen();
	}
	
	@Override
	public final void enter(GameState previousGameState) {
		gdxInput.setInputProcessor(new InputProcessor() {
			
			private int lastKeycode;

			@Override
			public boolean keyDown(int keycode) {
				lastKeycode = keycode;
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return AbstractMenuScreenGameState.this.keyTyped(character, lastKeycode);
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				return mouseClicked();
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				return false;
			}
			
		});
		
		enterScreen(previousGameState);
	}

	@Override
	public final void leave() {
		gdxInput.setInputProcessor(null);
		leaveScreen();
	}

	@Override
	public final void render() {
		renderer.setTransformMatrix(new Matrix4());
		if (screenTex != null) {
			renderer.drawFullscreenTexture(screen, alpha);
		}
		renderUI();
	}

	protected abstract void enterScreen(GameState previousGameState);
	protected abstract void leaveScreen();
	protected abstract void disposeScreen();
	public    abstract GameState update(float dt);
	protected abstract void renderUI();
	protected abstract boolean keyTyped(char character, int keycode);
	protected abstract boolean mouseClicked();
}
