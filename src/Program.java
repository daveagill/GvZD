

import week.of.awesome.ApplicationEventListener;
import week.of.awesome.Assets;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Program {

	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Granny v.s. Zombie Dinosaurs";
		config.width = 640;
		config.height = 480;
		
		config.addIcon(Assets.getIconPath("icon-128.png"), Assets.ASSET_FILE_TYPE);
		config.addIcon(Assets.getIconPath("icon-64.png") , Assets.ASSET_FILE_TYPE);
		config.addIcon(Assets.getIconPath("icon-32.png") , Assets.ASSET_FILE_TYPE);
		config.addIcon(Assets.getIconPath("icon-16.png") , Assets.ASSET_FILE_TYPE);
		
		new LwjglApplication(new ApplicationEventListener(), config);
	}
}
