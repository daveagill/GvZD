package week.of.awesome;

import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;

public interface Stage {
	
	public static float BLOCK_SIZE = 0.5f;
	
	public static float BLOCK_HEIGHT = 0.5f;
	public static float BLOCK_WIDTH = BLOCK_HEIGHT;

	public List<Integer> getHeightMap();
	public List<Body> getBodies();
	
	public float getLeftPosition();

	float heightAt(float x);
}
