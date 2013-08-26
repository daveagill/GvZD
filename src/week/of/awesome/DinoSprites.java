package week.of.awesome;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class DinoSprites {

	private Texture leftLegTex;
	private Texture rightLegTex;
	private Texture bodyTex;
	
	private Sprite leftLegSpr;
	private Sprite rightLegSpr;
	private Sprite bodySpr;
	
	private Assets assets;
	private float scale;
	
	public DinoSprites(Assets assets, float scale) {
		this.assets = assets;
		this.scale = scale;
	}
	
	public void setBody(String filename, int srcWidth, int srcHeight) {
		bodyTex = new Texture(assets.getSprite(filename));
		bodySpr = Renderer.sprite(bodyTex, srcWidth, srcHeight, scale);
	}
	
	public void setLeftLeg(String filename, int srcWidth, int srcHeight) {
		leftLegTex = new Texture(assets.getSprite(filename));
		leftLegSpr = Renderer.sprite(leftLegTex, srcWidth, srcHeight, scale);
		Renderer.setOrigin(leftLegSpr, 0, 1);
	}
	
	public void setRightLeg(String filename, int srcWidth, int srcHeight) {
		rightLegTex = new Texture(assets.getSprite(filename));
		rightLegSpr = Renderer.sprite(rightLegTex, srcWidth, srcHeight, scale);
		Renderer.setOrigin(rightLegSpr, 0, 1);
	}
	
	public float getScale() { return scale; }
	public Sprite getBodySprite() { return bodySpr; }
	public Sprite getLeftLegSprite() { return leftLegSpr; }
	public Sprite getRightLegSprite() { return rightLegSpr; }
	
	public void dispose() {
		leftLegTex.dispose();
		rightLegTex.dispose();
		bodyTex.dispose();
	}
}
