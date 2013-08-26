package week.of.awesome;

import com.badlogic.gdx.Input;

public class ReadingSignpostGameState extends AbstractMenuScreenGameState {

	private float timeout;
	private boolean acknowledged;
	private GameState backgroundGameState;
	
	public ReadingSignpostGameState(Assets assets, Renderer renderer, Input gdxInput) {
		super(assets, renderer, gdxInput);
	}
	
	public void setBackgroundGameState(GameState backgroundGameState) {
		this.backgroundGameState = backgroundGameState;
	}
	
	public void setSignpostScreenForLevel(Level level) {
		super.loadScreen("signposts/" + level.getSignPostFilename());
	}

	@Override
	protected void enterScreen(GameState previousGameState) {
		timeout = 2f; // must be onscreen for at least this many seconds
		acknowledged = false;
	}

	@Override
	protected void leaveScreen() {
	}

	@Override
	protected void disposeScreen() {
	}

	@Override
	public GameState update(float dt) {
		if (timeout > 0) {
			timeout -= dt;
		}
		
		return acknowledged ? backgroundGameState : this;
	}

	@Override
	protected void renderUI() {
	}

	@Override
	protected boolean keyTyped(char character, int keycode) {
		if (timeout > 0) { return false; }
		acknowledged = true;
		return true;
	}

	@Override
	protected boolean mouseClicked() {
		if (timeout > 0) { return false; }
		acknowledged = true;
		return true;
	}

}
