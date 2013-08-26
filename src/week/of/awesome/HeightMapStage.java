package week.of.awesome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class HeightMapStage implements Stage {
	
	private static float LEFT_OFFSCREEN_FLOOR_LENGTH = 5f;
	private static int ALERT_WIDTH = 40;

	private List<Integer> heightMap = new ArrayList<Integer>();
	private List<Body> bodies = new ArrayList<Body>();
	private float leftOffset = 0;
	
	private Body leftBlocker;
	private Body leftOffscreenFloor;
	
	private Physics physics;
	
	public HeightMapStage(Physics physics) {
		this.physics = physics;
		this.leftBlocker = physics.createPlayerBlocker(new Vector2(leftOffset, 0), 0.5f, 5f);
		this.leftOffscreenFloor = physics.createFloor(new Vector2(leftOffset-LEFT_OFFSCREEN_FLOOR_LENGTH, 0), LEFT_OFFSCREEN_FLOOR_LENGTH, BLOCK_HEIGHT);
	}
	
	public void dispose() {
		for (Body body : bodies) {
			physics.removeBody(body);
		}
	}
	
	public void appendSegment(LevelSegment segment) {
		int lastHeight = segment.getHeightMap()[0];
		int bodyWidth = 0;
		
		for (int x = 0; x < segment.length(); ++x) {
			int h = segment.getHeightMap()[x];
			
			if (h != lastHeight) {
				appendBodies(lastHeight, bodyWidth);
				
				lastHeight = h;
				bodyWidth = 0;
			}
			
			heightMap.add(h);
			++bodyWidth;
		}
		
		appendBodies(lastHeight, bodyWidth);
	}
	
	private void appendBodies(int height, int bodyWidth) {
		float x = (float)heightMap.size();
		Body body = physics.createFloor(new Vector2(x - bodyWidth, 0).mul(BLOCK_SIZE).add(leftOffset, 0), (float)bodyWidth * BLOCK_SIZE, (float)height * BLOCK_SIZE);
		body.setUserData(this);
		
		for (int i = 0; i < bodyWidth; ++i) {
			bodies.add(body);
		}
	}
	
	@Override
	public List<Integer> getHeightMap() {
		return Collections.unmodifiableList(heightMap);
	}
	
	public List<Body> getBodies() {
		return bodies;
	}
	
	public float getLeftPosition() {
		return leftOffset;
	}
	
	@Override
	public float heightAt(float x) {
		int sampleIdx = (int)((x - leftOffset) / BLOCK_WIDTH);
		if (sampleIdx >= heightMap.size()) { sampleIdx = heightMap.size() - 1; } // n.b. might produce -1, next line will handle it if so
		if (sampleIdx < 0) { return leftOffscreenFloor.getPosition().y + BLOCK_HEIGHT; }
		return (float)heightMap.get(sampleIdx) * BLOCK_WIDTH;
	}
	
	public boolean reachedEnd() {
		return heightMap.size() <= ALERT_WIDTH;
	}
	
	public void keepUpWith(Camera cam) {
		if (heightMap.isEmpty()) { return; }
		
		while (leftOffset + BLOCK_WIDTH - cam.getPosition().x < 0) {
			boolean disposeBody = !bodies.isEmpty() && (bodies.size() == 1 || bodies.get(0) != bodies.get(1));
			if (disposeBody) {
				physics.removeBody(bodies.get(0));
			}
			bodies.remove(0);
			heightMap.remove(0);
			leftOffset += BLOCK_WIDTH;
		}
		
		leftBlocker.setTransform(leftOffset, 0, 0);
		leftOffscreenFloor.setTransform(leftOffset-LEFT_OFFSCREEN_FLOOR_LENGTH, heightMap.get(0) * BLOCK_HEIGHT - BLOCK_HEIGHT, 0);
	}
}
