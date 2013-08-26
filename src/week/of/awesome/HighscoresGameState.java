package week.of.awesome;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class HighscoresGameState extends AbstractMenuScreenGameState {
	
	private static final Rectangle tableRect = new Rectangle(100, 90, 650, 290);
	
	private Highscores highscores;
	
	private boolean acknowledged;
	private Map<GameState, GameState> nextGameStates = new HashMap<GameState, GameState>();
	private GameState nextGameState;

	public HighscoresGameState(Assets assets, Renderer renderer, Input gdxInput, Highscores highscores) {
		super(assets, renderer, gdxInput);
		this.highscores = highscores;
		super.loadScreen("highscores.png");
	}
	
	public void addNextGameState(GameState previous, GameState next) {
		nextGameStates.put(previous, next);
	}

	@Override
	protected void enterScreen(GameState previousGameState) {
		acknowledged = false;
		nextGameState = nextGameStates.get(previousGameState);
	}

	@Override
	protected void leaveScreen() {
	}

	@Override
	protected void disposeScreen() {
	}

	@Override
	public GameState update(float dt) {
		return acknowledged ? nextGameState : this;
	}

	@Override
	protected void renderUI() {
		float tableLeftX = tableRect.getX();
		float tableRightX = tableRect.getX() + tableRect.getWidth();
		float tableTopY = renderer.getScreenHeight() - tableRect.getY();
		
		for (int i = 0; i < highscores.numHighscores(); ++i) {
			Highscores.Entry entry = highscores.getHighscores().get(i);
			
			float entryY = renderer.getTextLineHeightFromTopY(tableTopY, i);
			float entryYBottom = renderer.getTextLineHeightFromTopY(entryY, 1);

			if (entryYBottom < tableTopY - tableRect.getHeight()) {
				break;
			}
			
			String leftText = (i+1) + ".     " + entry.getName().toUpperCase();
			String rightText = String.valueOf(entry.getScore());
			renderer.drawLeftAlignedText(leftText, new Vector2(tableLeftX, entryY));
			renderer.drawRightAlignedText(rightText, new Vector2(tableRightX, entryY));
		}
		

	}

	@Override
	protected boolean keyTyped(char character, int keycode) {
		acknowledged = true;
		return true;
	}

	@Override
	protected boolean mouseClicked() {
		acknowledged = true;
		return true;
	}

}
