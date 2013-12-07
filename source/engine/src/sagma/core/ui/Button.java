package sagma.core.ui;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.math.Vec2;
import sagma.core.ui.UIComponent;

public class Button extends UIComponent{
	
	public Button(){
		setLocation(0, 0);
		setSize(.2f, .2f);
	}
	
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		Vec2 pos = getLocation();
		System.out.println(pos);
		Vec2 size = getSize();
		
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -1f);
		
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2f(pos.x - 0.5f, -pos.y +0.5f);
		gl.glVertex2f(pos.x + size.x - 0.5f, -pos.y +0.5f);
		gl.glVertex2f(pos.x + size.x - 0.5f, -pos.y - size.y +0.5f);
		gl.glVertex2f(pos.x - 0.5f, -pos.y - size.y +0.5f);
		gl.glEnd();
		
		gl.glPopMatrix();
		
	}
}
