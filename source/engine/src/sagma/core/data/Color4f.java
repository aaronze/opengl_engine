package sagma.core.data;

import javax.media.opengl.GL2;

/**
 * 
 * Represents a color with a fourth component for transparency. 
 * </br>Used in {@link sagma.core.generator.color.ColorGenerator ColorGenerator}
 * 
 * @author Aaron Kison
 *
 */
public class Color4f {
	public static final Color4f WHITE = new Color4f(1.0f, 1.0f, 1.0f);
	public static final Color4f BLACK = new Color4f(0.0f, 0.0f, 0.0f);
	public static final Color4f RED = new Color4f(1.0f, 0.0f, 0.0f);
	public static final Color4f GREEN = new Color4f(0.0f, 1.0f, 0.0f);
	public static final Color4f BLUE = new Color4f(0.0f, 0.0f, 1.0f);
	public static final Color4f GRAY = new Color4f(0.6f, 0.6f, 0.6f);
	
	public static final Color4f MAGENTA = new Color4f(1.0f, 0.0f, 1.0f);
	public static final Color4f YELLOW = new Color4f(1.0f, 1.0f, 0.0f);
	public static final Color4f CYAN = new Color4f(0.0f, 1.0f, 1.0f);
	
	public static final Color4f GOLD = new Color4f(1.0f, 0.843f, 0.0f);
	
	private float red, green, blue, alpha;
	
	public Color4f(float r, float g, float b) {
		red = r;
		green = g;
		blue = b;
	}
	
	public Color4f(float r, float g, float b, float a) {
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}
	
	public Color4f(Color4f color) {
		red = color.getRed();
		green = color.getGreen();
		blue = color.getBlue();
		alpha = color.getAlpha();
	}
	
	public void apply(GL2 gl) {
		gl.glColor4f(red, green, blue, alpha);
	}
	
	public float getRed() {return red;}
	public float getGreen() {return green;}
	public float getBlue() {return blue;}
	public float getAlpha() {return alpha;}
	public void set(float r, float g, float b, float a) {
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}
	public void setRed(float r) {red = r;}
	public void setGreen(float g) {green = g;}
	public void setBlue(float b) {blue = b;}
	public void setAlpha(float a) {alpha = a;}
	
	public void set(Color4f color) {
		red = color.getRed();
		green = color.getGreen();
		blue = color.getBlue();
		alpha = color.getAlpha();
	}
}
