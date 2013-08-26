package week.of.awesome;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Disposable;

public class Renderer implements Disposable {

	private GLCommon gl;
	
	private int screenWidth;
	private int screenHeight;
	
	private SpriteBatch spriteBatch;
	private BitmapFont font;

	
	public Renderer(Graphics gfx, Assets assets) {
		gl = gfx.getGLCommon();
		screenWidth = gfx.getWidth();
		screenHeight = gfx.getHeight();
		
		spriteBatch = new SpriteBatch();
		font = new BitmapFont(assets.getFont("DINk.fnt"), assets.getFont("DINk.png"), false);
		
		gl.glClearColor(0, 0, 0, 1);
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
	public float getTextLineHeightFromTopY(float topY, int line) {
		return topY - font.getLineHeight() * line;
	}
	
	public float getTextLineHeightFromTop(int line) {
		return getTextLineHeightFromTopY(getScreenHeight(), line);
	}
	
	
	public void begin() {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.begin();
	}
	
	public void end() {
		spriteBatch.end();
	}
	
	
	public void setTransformMatrix(Matrix4 transform) {
		spriteBatch.setTransformMatrix(transform);
	}
	
	public Matrix4 getCombinedMatrix() {
		return spriteBatch.getProjectionMatrix().cpy().mul(spriteBatch.getTransformMatrix());
	}
	
		
	public void drawLeftAlignedText(CharSequence text, Vector2 position) {
		font.draw(spriteBatch, text, position.x, position.y);
	}
	
	public void drawRightAlignedText(CharSequence text, Vector2 position) {
		font.draw(spriteBatch, text , position.x - getTextWidth(text), position.y);
	}
	
	public float getTextWidth(CharSequence text) {
		return font.getBounds(text).width;
	}
	
	
	public void drawCenterBottom(Sprite sprite, Vector2 at, boolean mirrorX) {
		// characters are positioned center-bottom
		float scaleX = mirrorX ? -1 : 1;
		sprite.setScale(scaleX, 1);
		setPositionOfOrigin(sprite, at.x - sprite.getWidth() / 2 * scaleX, at.y);
		sprite.draw(spriteBatch);
	}
	
	public void drawAtWithHeight(Sprite sprite, Vector2 at, float height) {
		setPositionOfOrigin(sprite, at.x, at.y);
		sprite.setScale(1, height);
		sprite.draw(spriteBatch);
	}
	
	public void drawCenteredAt(Sprite sprite, Vector2 at) {
		setPositionOfOrigin(sprite, at.x - sprite.getWidth() / 2, at.y - sprite.getHeight() / 2);
		sprite.draw(spriteBatch);
	}
	
	public void drawAtWithAngle(Sprite sprite, Vector2 at, float angle) {
		setPositionOfOrigin(sprite, at.x, at.y);
		sprite.setRotation(angle);
		sprite.draw(spriteBatch);
	}
	
	public void drawAtScreenOffsetWithAngle(Sprite sprite, Vector2 at, Vector2 screenOffset, float angle, boolean mirrorX, boolean mirrorY) {
		float scaleX = mirrorX ? -1 : 1;
		float scaleY = mirrorY ? -1 : 1;
		Vector2 pos = Camera.stow(screenOffset.mul(scaleX, scaleY)).add(at);
		setPositionOfOrigin(sprite, pos.x, pos.y);
		sprite.setRotation(angle);
		sprite.setScale(scaleX, scaleY);
		sprite.draw(spriteBatch);
	}
	
	public void drawFullscreenTexture(TextureRegion screen, float alpha) {
		spriteBatch.setColor(1, 1, 1, alpha);
		float x = screenWidth/2f - screen.getRegionWidth()/2f;
		float y = screenHeight/2f - screen.getRegionHeight()/2f;
		spriteBatch.draw(screen, x, y);
		spriteBatch.setColor(1, 1, 1, 1);
	}
	
	/** For some reason setOrigin doesn't influence setPosition, this method makes up for that **/
	public static void setPositionOfOrigin(Sprite sprite, float x, float y) {
		sprite.setPosition(x - sprite.getOriginX(), y - sprite.getOriginY());
	}
	
	public static Sprite sprite(Texture texture, int srcWidth, int srcHeight, float scale) {
		Sprite s = new Sprite(texture, srcWidth, srcHeight);
		s.setSize(Camera.stow((float)srcWidth * scale), Camera.stow((float)srcHeight * scale));
		s.setOrigin(0, 0);
		return s;
	}
	
	public static void setOrigin(Sprite sprite, float hwPercent, float hhPercent) {
		float px = sprite.getWidth() / 2f + sprite.getWidth() / 2f * hwPercent;
		float py = sprite.getHeight() / 2f + sprite.getHeight() / 2f * hhPercent;
		sprite.setOrigin(px, py);
	}

	@Override
	public void dispose() {
		font.dispose();
		spriteBatch.dispose();
	}
}
