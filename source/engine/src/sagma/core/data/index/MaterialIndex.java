package sagma.core.data.index;


import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.Mesh;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.model.State;
import sagma.core.render.Render;

public class MaterialIndex extends Instance {
	private Mesh mesh;
	
	public MaterialIndex(Mesh m) {
		mesh = m;
		setPickable(false);
		setSolid(false);
	}
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		if (Render.renderPass == Render.RENDER_DRAW && isVisible()) {
			GL2 gl = drawable.getGL().getGL2();
	        
			gl.glPushMatrix();
			
	        mesh.activate(gl);
	        
	        float rotX = 0;
	        float rotY = 0;
	        float rotZ = 0;
	        
			for (Instance i : children) {
				State curState = i.getState();
				Vec3 curScale = i.scale;
				
				gl.glPushMatrix();
	        	if (curState.getRotX() != rotX) {
	        		rotX = curState.getRotX();
	        		gl.glRotatef(rotX, 1, 0, 0);
	        	}
	        	if (curState.getRotY() != rotY) {
	        		rotY = curState.getRotY();
	        		gl.glRotatef(rotY, 0, 1, 0);
	        	}
	        	if (curState.getRotZ() != rotZ) {
	        		rotZ = curState.getRotZ();
	        		gl.glRotatef(rotZ, 0, 0, 1);
	        	}
		        gl.glTranslatef(curState.getX(), curState.getY(), curState.getZ());
		        gl.glScalef(curScale.x, curScale.y, curScale.z);
		        
		        gl.glDrawArrays(GL.GL_TRIANGLES, 0, mesh.trigs);
		        
		        gl.glPopMatrix();
			}
			
			mesh.deactivate(gl);
			
			gl.glPopMatrix();
		}
	}

	@Override
	public void step() {
		for (Instance i : children) i.step();
	}
	
	public int size() {
		return children.size();
	}
}
