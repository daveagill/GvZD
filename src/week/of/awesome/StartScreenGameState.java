package week.of.awesome;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;

public class StartScreenGameState extends AbstractMenuScreenGameState {

	private boolean acknowledged;
	private boolean showHighscores;
	
	private GameState nextGameState;
	private GameState showHighscoresGameState;
	
	public StartScreenGameState(Assets assets, Renderer renderer, Input gdxInput) {
		super(assets, renderer, gdxInput);
		super.loadScreen("start.png");
	}
	
	public void setNextGameState(GameState nextGameState) {
		this.nextGameState = nextGameState;
	}
	
	public void setShowHighscoresGameState(GameState showHighscoresGameState) {
		this.showHighscoresGameState = showHighscoresGameState;
	}

	@Override
	protected void enterScreen(GameState previousGameState) {
		acknowledged = false;
		showHighscores = false;
	}

	@Override
	protected void leaveScreen() {
	}

	@Override
	protected void disposeScreen() {
	}
	
	@Override
	public GameState update(float dt) {
		if (showHighscores) { return showHighscoresGameState; }
		return acknowledged ? nextGameState : this;
	}

	@Override
	protected void renderUI() {
	}

	@Override
	protected boolean keyTyped(char character, int keycode) {
		if (character == 'h' || character == 'H') {
			showHighscores = true;
			return true;
		} else if (keycode == Keys.SPACE) {
			acknowledged = true;
			return true;
		}
		
		return false;
	}

	@Override
	protected boolean mouseClicked() {
		return false;
	}

	
}
