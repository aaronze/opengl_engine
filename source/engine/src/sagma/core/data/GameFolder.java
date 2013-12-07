package sagma.core.data;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import sagma.core.render.GameLoader;

/**
 * Represents a folder containing games
 * TODO To be implemented fully
 * 
 * @author Aaron Kison
 *
 */
public class GameFolder {
	private ArrayList<GameFolder> files;
	private GameInfo contents;
	private boolean isVisible = false;
	private boolean isExpanded = false;
	
	public GameFolder(GameInfo game) {
		files = new ArrayList<GameFolder>();
		contents = game;
	}
	
	public boolean isExpanded() {
		return isExpanded;
	}
	
	public void setVisible(boolean b) {
		isVisible = b;
	}
	
	public void drawOn(Graphics g, int x, int y, int w, int h, boolean selected) {
		if (!isVisible) return;
		
		int xi = x + w;
		int yi = GameLoader.OFFSET_Y;
		if (isExpanded) {
			// Draw children
			Iterator<GameFolder> children = files.iterator();
			while (children.hasNext()) {
				GameFolder folder = children.next();
				folder.drawOn(g, xi, yi, w, h, selected);
				yi += h;
				if (yi >= GameLoader.SCREEN_HEIGHT - h) {
					yi = GameLoader.OFFSET_Y;
					xi += w;
				}
			}
		}
		
		// Draw self
		contents.drawOn(g, x, y, w, h, selected);
	}
}
