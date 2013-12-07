package sagma.core.material;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import sagma.core.light.LightManager;
import sagma.core.math.Vec3;
import sagma.core.render.Render;

public class Composition extends Material {
	private static Shader compShader;
	
	private Texture diffuse;
	private Texture emissive;
	private Texture bump;
	private Texture normal;
	
	public Composition(GL2 gl) {
		if (compShader == null) {
			compShader = new Shader("Shaders/Lighting/Lighting");
			
			compShader.activate(gl);
			int diffuseLoc = gl.glGetUniformLocation(compShader.getID(), "texDiffuse");
			gl.glUniform1iARB(diffuseLoc, 0);
			int normalLoc = gl.glGetUniformLocation(compShader.getID(), "texNormal");
			gl.glUniform1iARB(normalLoc, 1);
			int emissiveLoc = gl.glGetUniformLocation(compShader.getID(), "texEmissive");
			gl.glUniform1iARB(emissiveLoc, 2);
			int bumpLoc = gl.glGetUniformLocation(compShader.getID(), "texBump");
			gl.glUniform1iARB(bumpLoc, 3);
			compShader.deactivate(gl);
		}
	}
	
	public void setDiffuse(Texture t) {
		diffuse = t;
	}
	
	public void setEmissive(Texture t) {
		emissive = t;
	}
	
	public void setBump(Texture t) {
		bump = t;
	}
	
	public void setNormal(Texture t) {
		normal = t;
	}
	
	@Override
	public void activate(GL2 gl) {
		gl.glPushAttrib(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		compShader.activate(gl);
		
		int loc = gl.glGetUniformLocation(compShader.getID(), "eyeVec");
		Vec3 pos = Render.camera.getLocation();
		gl.glUniform3f(loc, pos.x, pos.y, pos.z);
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		gl.glActiveTexture(GL.GL_TEXTURE0 + 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, diffuse.getID());
		
		gl.glActiveTexture(GL.GL_TEXTURE0 + 1);
		gl.glBindTexture(GL.GL_TEXTURE_2D, normal.getID());
		
		gl.glActiveTexture(GL.GL_TEXTURE0 + 2);
		gl.glBindTexture(GL.GL_TEXTURE_2D, emissive.getID());
		
		gl.glActiveTexture(GL.GL_TEXTURE0 + 3);
		gl.glBindTexture(GL.GL_TEXTURE_2D, bump.getID());
		
	}
	
	@Override
	public void deactivate(GL2 gl) {
		compShader.deactivate(gl);
		
		gl.glDisable(GL.GL_TEXTURE_2D);
		
		gl.glPopAttrib();
	}
}
