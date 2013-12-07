package sagma.games.rts.client;

import java.util.LinkedList;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.Color4f;
import sagma.core.model.Instance;
import sagma.core.render.FastText;
import sagma.core.render.Text;


public class Chat extends Instance {  //TODO change this to a UIComponent
	private LinkedList<Color4f> color = new LinkedList<Color4f>();
	private LinkedList<String> lines = new LinkedList<String>();
	private FastText text = new FastText();
	private Color4f currentColor = Color4f.WHITE;
	public static Color4f DEFAULT_COLOR = Color4f.WHITE;

	public Chat() {
		setPickable(false);
		setSolid(false);
	}

	public void setColor(Color4f col) {
		currentColor = col;
	}

	public void resetColor() {
		currentColor = DEFAULT_COLOR;
	}

	public void addLine(String line) {
		while (lines.size() > 32) {
			lines.removeFirst();
			color.removeFirst();
		}

		int i = 0;
		for (i = 0; i < line.length()-64; i += 64) {
			String s = line.substring(i, i+64);
			lines.addLast(s);
			color.addLast(currentColor);
		}
		String s = line.substring(i, line.length());
		lines.addLast(s);
		color.addLast(currentColor);

		update();
	}

	private void update() {
		text.clear();

		int size = lines.size();
		for (int i = 0; i < size; i++) {
			String s = lines.get(size-i-1);
			Text t = new Text(s, 10, 80+i*20);
			t.col = color.get(size-i-1);
			text.add(t);
		}
	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		text.draw(drawable);
	}

}
