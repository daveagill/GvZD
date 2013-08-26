package week.of.awesome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class Highscores {
	public static class Entry {
		private String name;
		private int score;
		
		public Entry(String name, int score) {
			this.name = name;
			this.score = score;
		}
		
		public String getName() { return name; }
		public int getScore() { return score; }
	}
	
	private static final int MAX_SCORES = 10;
	
	private FileHandle xmlFile;
	private List<Entry> highScores = new ArrayList<Entry>();
	
	public Highscores(FileHandle xmlFile) {
		this.xmlFile = xmlFile;
		load();
	}
	
	public boolean isHighscore(int score) {
		return highScores.size() < MAX_SCORES || highScores.get(highScores.size()-1).getScore() < score;
	}

	
	public void addScore(String user, int score) {
		
		if (!isHighscore(score)) { return; }
		
		// locate correct insertion point
		int i = highScores.size() - 1;
		while (i >= 0 && score > highScores.get(i).score) { --i; }
		highScores.add(i+1, new Entry(user, score));
		
		// ensure we don't exceed MAX_SCORES
		if (highScores.size() > MAX_SCORES) {
			highScores.remove(highScores.size()-1);
		}
		
		// immediately persist it
		save();
	}
	
	public int numHighscores() {
		return highScores.size();
	}
	
	public List<Entry> getHighscores() {
		return Collections.unmodifiableList(highScores);
	}
	

	private void load() {
		XmlReader reader = new XmlReader();
		Element highscoresElement = null;
		try {
			highscoresElement = reader.parse(xmlFile);
		} catch (IOException ioe) {
			final int devilScore = 666;
			addScore("Unable to load scores :-(", devilScore);
		}
		
		if (highscoresElement != null) {
		
			for (int i = 0; i < highscoresElement.getChildCount(); ++i) {
				String name = "";
				int score = 0;
				
				ObjectMap<String, String> childAttrs = highscoresElement.getChild(i).getAttributes();
				for (ObjectMap.Entry<String, String> attr : childAttrs.entries()) {
					String attrName = attr.key;
					String attrValue = attr.value;
					
					if (attrName.equalsIgnoreCase("name")) {
						name = attrValue;
					}
					else if (attrName.equalsIgnoreCase("score")) {
						score = Integer.valueOf(attrValue);
					}
				}
				
				addScore(name, score);
			}
			
		}
	}
	
	private void save() {
		try {
			XmlWriter writer = new XmlWriter(xmlFile.writer(false));
			
			writer.element("highscores"); // <highscores>
			
			for (Entry entry : highScores) {
				writer.element("entry").
					attribute("name", entry.getName()).
					attribute("score", entry.getScore()).
					pop(); // <entry name="user" score="score" />
			}
			
			
			writer.pop(); // </highscores>
			
			writer.close();
		} catch (IOException e) {
			// unable to write scores, nevermind
		}
	}
}
