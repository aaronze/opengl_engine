package sagma.core.model;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.math.Vec3;
import sagma.core.model.bounding.BoundingSphere;
import sagma.core.render.BitmapText;

public class TextInstance extends Instance {
	private String text;
	
	public TextInstance(String text, float x, float y) {
		super();
		
		model = new Model();
		model().bounds = new BoundingSphere(1.0f, 1.0f);
		
		this.text = text;
		setPosition(x, y, 0);
		
		setPickable(false);
		setSolid(false);
	}
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glPushMatrix();
		gl.glDisable(GL.GL_DEPTH_TEST);
		
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		Vec3 pos = getPosition();
		BitmapText.drawText(gl, text, pos.x, pos.y, 3);
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glPopMatrix();
	}
	
	public void setText(String text){
		this.text = text;
	}
}
