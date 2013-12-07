package sagma.core.material;

import javax.media.opengl.GL2;

import sagma.core.data.Color4f;

public class SolidColor extends Material {
	private Color4f color;
	
	public static SolidColor WHITE = new SolidColor(Color4f.WHITE);
	
	public SolidColor(Color4f color) {
		this.color = color;
	}
	
	@Override
	public void activate(GL2 gl) {
		gl.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	@Override
	public void deactivate(GL2 gl) {
		gl.glColor4f(1, 1, 1, 1);
	}
}
