package sagma.core.particle;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.Color4f;
import sagma.core.math.Vec3;
import sagma.core.model.State;
import sagma.core.render.Drawable;

public class Particle implements Drawable {
	private State state;
	private Color4f color;
	private float size;
	private float timeToLive;
	private boolean isAlive;
	private Vec3 v1;
	private Vec3 v2;
	private Vec3 v3;
	
	public Particle(State initialState, Color4f color, float size, float timeToLive) {
		state = initialState;
		this.color = color;
		this.size = size;
		this.timeToLive = timeToLive;
		isAlive = true;
	}
	
	public void setTriangle(Vec3 a, Vec3 b, Vec3 c) {
		v1 = a;
		v2 = b;
		v3 = c;
	}
	
	public boolean isAlive() {return isAlive;}
	public State state() {return state;}
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		if (!isAlive) return;
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glPushMatrix();
		gl.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		
		Vec3 location = state.getPosition();
		Vec3 rotation = state.getRotation();
		gl.glTranslatef(location.x, location.y, location.z);
		gl.glRotatef(rotation.x, 1, 0, 0);
		gl.glRotatef(rotation.y, 0, 1, 0);
		gl.glRotatef(rotation.z, 0, 0, 1);
		
		gl.glBegin(GL.GL_TRIANGLES);
			if (v1 == null) {
				gl.glVertex3f(0.0f, 0.0f, 0.0f);
				gl.glVertex3f(size, 0.0f, 0.0f);
				gl.glVertex3f(0.0f, size, 0.0f);
			} else {
				gl.glVertex3f(v1.x, v1.y, v1.z);
				gl.glVertex3f(v2.x, v2.y, v2.z);
				gl.glVertex3f(v3.x, v3.y, v3.z);
			}
		gl.glEnd();
		
		gl.glPopMatrix();
	}
	
	public void step() {
		if (!isAlive) return;
		
		state.step();
		
		timeToLive--;
		if (timeToLive <= 0) {
			isAlive = false;
		}
	}

}
