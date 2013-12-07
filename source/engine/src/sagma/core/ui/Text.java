package sagma.core.ui;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.model.Instance;
import sagma.core.render.Render;

import com.jogamp.opengl.util.awt.TextRenderer;

/**
 * TODO - For mining demonstration due to Model.Text not working as expected.
 * Remove when unnecessary
 * @author Michael
 */
public class Text extends Instance {

		private String text;
		private int x;
		private int y;
		
		public Text(String text, int x, int y){
			this.text = text;
			this.x = x;
			this.y = y;
			
			setPickable(false);
			setSolid(false);
		}
		
		public void setText(String text){
			this.text = text;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public String getText() {
			return text;
		}
		
		@Override
		public void draw(GLAutoDrawable draw) {
			TextRenderer render = Render.textRender;
			render.beginRendering(Render.WIDTH, Render.HEIGHT);
			render.draw(text, x, y);
			render.endRendering();
		}
}
