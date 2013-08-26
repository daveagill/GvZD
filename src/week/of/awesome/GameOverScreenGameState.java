package week.of.awesome;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class GameOverScreenGameState extends AbstractMenuScreenGameState {

	private static final String legalCharacters =
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
			"abcdefghijklmnopqrstuvwxyz" +
			"1234567890" +
			"!\"£$%^&*()-=_+[]{};'#:@~,./<>?\\|`¬'" +
			" ";
	
	private static final int MAX_HIGHSCORE_NAME_WIDTH_PX = 350;
	
	private ScoreStats scoring;
	private Highscores highscores;
	
	private Vector2 distanceTextPos;
	private Vector2 killCountTextPos;
	private Vector2 scoreTextPos;
	private Vector2 highscoreTextPos;
	private Vector2 enterNameTextPos;
	
	private boolean isHighscore;
	private boolean acknowledged;
	
	private static final float BLINK_ON_DURATION = 1f;
	private static final float BLINK_OFF_DURATION = 0.3f;
	private float timeRemainingToBlink;
	private boolean isBlinkedOn;
	private String highscoreName;
	
	private GameState restartGameState;
	private GameState showHighscoresGameState;
	
	public GameOverScreenGameState(Assets assets, Renderer renderer, Input gdxInput, ScoreStats scoring, Highscores highscores) {
		super(assets, renderer, gdxInput);
		super.loadScreen("gameover.png");
		this.scoring = scoring;
		this.highscores = highscores;
		
		distanceTextPos = new Vector2(0, renderer.getTextLineHeightFromTop(0));
		killCountTextPos = new Vector2(0, renderer.getTextLineHeightFromTop(1));
		scoreTextPos = new Vector2(renderer.getScreenWidth(), renderer.getTextLineHeightFromTop(0));
		highscoreTextPos = new Vector2(260, 150);
		enterNameTextPos = new Vector2(50, 70);
	}
	
	public void setRestartGameState(GameState restart) {
		this.restartGameState = restart;
	}
	
	public void setShowHighscoresGameState(GameState showHighscores) {
		this.showHighscoresGameState = showHighscores;
	}

	@Override
	protected void enterScreen(GameState previousGameState) {
		acknowledged = false;
		isHighscore = highscores.isHighscore(scoring.getScore());
		
		timeRemainingToBlink = BLINK_ON_DURATION;
		isBlinkedOn = true;
		highscoreName = "";
	}

	@Override
	protected void leaveScreen() {
	}
	
	@Override
	protected void disposeScreen() {
	}
	
	@Override
	public GameState update(float dt) {
		timeRemainingToBlink -= dt;
		if (timeRemainingToBlink <= 0) {
			isBlinkedOn = !isBlinkedOn;
			if (isBlinkedOn) {
				timeRemainingToBlink += BLINK_ON_DURATION;
			} else {
				timeRemainingToBlink += BLINK_OFF_DURATION;
			}
		}
		
		if (acknowledged) {
			if (isHighscore) {
				return showHighscoresGameState;
			} else {
				return restartGameState;
			}
		}
		
		return this;
	}

	@Override
	protected void renderUI() {
		String distanceText = "Distance Travelled: " + scoring.getDistanceTravelled() + "m";
		String killsText = "Kills: " + scoring.getKillCount();
		String scoreText = "Score: " + scoring.getScore();

		renderer.drawLeftAlignedText(distanceText, distanceTextPos);
		renderer.drawLeftAlignedText(killsText, killCountTextPos);
		renderer.drawRightAlignedText(scoreText , scoreTextPos);
		
		if (isHighscore) {
			if (isBlinkedOn) {
				String highscoreText = "NEW HIGH SCORE!";
				renderer.drawLeftAlignedText(highscoreText, highscoreTextPos);
			}
			

			String enterNameText = "SCOREBOARD NAME:   " + highscoreName;
			renderer.drawLeftAlignedText(enterNameText, enterNameTextPos);
		}
	}

	@Override
	protected boolean keyTyped(char character, int keycode) {
		
		if (isHighscore) {
			if (isLegalCharacter(character)) {
				if (renderer.getTextWidth(highscoreName) < MAX_HIGHSCORE_NAME_WIDTH_PX) {
					highscoreName = highscoreName + character;
				}
			}
			else if (keycode == Keys.BACKSPACE && !highscoreName.isEmpty()) {
				highscoreName = highscoreName.substring(0, highscoreName.length()-1);
			}
			
			if (keycode == Keys.ENTER) {
				acknowledged = true;
				
				highscores.addScore(highscoreName, scoring.getScore());
			}
		}
		else {
			acknowledged = true;
		}
		
		return true;
	}

	@Override
	protected boolean mouseClicked() {
		if (!isHighscore) { // allow mouse click as long as we don't need a highscore name
			acknowledged = true;
		}
		return true;
	}
	
	private boolean isLegalCharacter(char character) {
		return legalCharacters.contains("" + character);
	}

}
