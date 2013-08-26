package week.of.awesome;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;

public class Assets {
	
	public static FileType ASSET_FILE_TYPE = FileType.Internal;

	public FileHandle getMusic(String filename) {
		return asset("music/" + filename);
	}
	
	public FileHandle getSound(String filename) {
		return asset("sounds/" + filename);
	}
	
	public FileHandle getSprite(String filename) {
		return asset("sprites/" + filename);
	}
	
	public FileHandle getScreen(String filename) {
		return asset("screens/" + filename);
	}
	
	public FileHandle getFont(String filename) {
		return asset("fonts/" + filename);
	}
	
	public FileHandle getHighScoreXML() {
		return Gdx.files.local("highscores.xml");
	}
	
	public static String getIconPath(String filename) {
		return getAssetPath("icons/" + filename);
	}
	
	private static FileHandle asset(String path) {
		return Gdx.files.internal(getAssetPath(path));
	}
	
	private static String getAssetPath(String path) {
		return "assets/" + path;
	}
}
