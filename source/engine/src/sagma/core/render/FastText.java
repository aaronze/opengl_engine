package sagma.core.render;

import java.util.ArrayList;

import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.awt.TextRenderer;

import sagma.core.data.Color4f;
import sagma.core.model.Instance;

public class FastText extends Instance {
	private ArrayList<Text> text = new ArrayList<Text>();
	
	public FastText() {
		setPickable(false);
		setSolid(false);
	}
	
	public void add(Text t) {
		this.text.add(t);
	}
	
	public void clear() {
		text.clear();
	}
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		TextRenderer render = Render.textRender;
		render.beginRendering(Render.WIDTH, Render.HEIGHT);
		
		Text[] ts = text.toArray(new Text[1]);
		for (Text t : ts) {
			try {
				Color4f col = t.col;
				render.setColor(col.getRed(), col.getGreen(), col.getBlue(), 1.0f);
				render.draw(t.getText(), t.getX(), t.getY());
			} catch (Exception e) {
				System.out.println("Failed to make: " + t.getText());
			}
		}

		render.endRendering();
	}
}
