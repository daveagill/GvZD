package week.of.awesome;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.Sound;

public class SoundEffects {

	private Sound shot;
	private Sound dinoDie;
	
	public SoundEffects(Assets assets, Audio audio) {
		this.shot = audio.newSound(assets.getSound("163456__lemudcrab__pistol-shot.wav"));
		this.dinoDie = audio.newSound(assets.getSound("121620__jjwallace__flash-character-die.mp3"));
	}
	
	public void dispose() {
		shot.dispose();
		dinoDie.dispose();
	}
	
	public void shoot() {
		shot.play();
	}
	
	public void dinoDeath() {
		dinoDie.play();
	}
}
