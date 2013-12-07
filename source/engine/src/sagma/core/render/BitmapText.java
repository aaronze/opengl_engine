package sagma.core.render;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.gl2.GLUT;

public class BitmapText implements Drawable {
	private float x, y;
	private String text;
	private int font;
	
	/**
	 * Draws text onto the screen
	 * At last implementation the screen starts in the bottom left hand corner at -5.5, -4.0
	 *  and goes to the right hand corner at 5.5, 4.0. Text in the middle of the screen is obviously at 0, 0
	 * 
	 * @param gl GL2 context to draw on
	 * @param text String to display
	 * @param x Horizontal location from -5.5 to 5.5
	 * @param y Vertical location from -4.0 to 4.0
	 * @param font Font type to use, use 2 for default
	 */
	public static void drawText(GL2 gl, String text, float x, float y, int font) {
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glRasterPos2f(x, y);
		final GLUT glut = new GLUT();
		glut.glutBitmapString(GLUT.BITMAP_9_BY_15, text);
		
		gl.glPopMatrix();
	}
	
	public BitmapText(String text, float xPosition, float yPosition, int font) {
		x = xPosition;
		y = yPosition;
		this.text = text;
		this.font = font;
	}
	
	public BitmapText(String text, float xPosition, float yPosition) {
		x = xPosition;
		y = yPosition;
		this.text = text;
		font = 2;
	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glRasterPos2f(x, y);
		final GLUT glut = new GLUT();
		glut.glutBitmapString(font, text);
		
		gl.glPopMatrix();
	}
	
}
