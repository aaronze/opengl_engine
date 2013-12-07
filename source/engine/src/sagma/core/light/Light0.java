package sagma.core.light;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLLightingFunc;

import sagma.core.data.Color4f;
import sagma.core.render.Game;

public class Light0 {
	public final static int lightId = GLLightingFunc.GL_LIGHT0;
	public static boolean isEnabled = false;
	
	private final static float[] diffuseColor = new float[]{1,1,1,1};
	private final static float[] specularColor = new float[]{1,1,1,1};
	private final static float[] ambientColor = new float[]{1,1,1,1};
	private final static float[] position = new float[]{0,0,0,0};
	private final static float[] direction = new float[]{0,0,0,0};
	private static float angle = 15.0f;
	private static float exponent = 1.0f;
	private static float constantAtt = 0.0f;
	private static float linearAtt = 0.0f;
	private static float quadraticAtt = 0.0f;
	
	public final static void setEnabled(boolean enabled) {
		isEnabled = enabled;
		
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		if (isEnabled) gl.glEnable(lightId);
		else gl.glDisable(lightId);
	}
	
	public final static void setDiffuseColor(Color4f color) {
		diffuseColor[0] = color.getRed();
		diffuseColor[1] = color.getGreen();
		diffuseColor[2] = color.getBlue();
		diffuseColor[3] = color.getAlpha();
		
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		gl.glLightfv(lightId, GLLightingFunc.GL_DIFFUSE, diffuseColor, 0);
	}
	
	public final static void setAmbientColor(Color4f color) {
		ambientColor[0] = color.getRed();
		ambientColor[1] = color.getGreen();
		ambientColor[2] = color.getBlue();
		ambientColor[3] = color.getAlpha();
		
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		gl.glLightfv(lightId, GLLightingFunc.GL_AMBIENT, ambientColor, 0);
	}
	
	public final static void setSpecularColor(Color4f color) {
		specularColor[0] = color.getRed();
		specularColor[1] = color.getGreen();
		specularColor[2] = color.getBlue();
		specularColor[3] = color.getAlpha();
		
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		gl.glLightfv(lightId, GLLightingFunc.GL_SPECULAR, specularColor, 0);
	}
	
	/**
	 * Moves the light-source to a position described by a FOUR dimensional
	 * float array. For example: { 0.4f, 0.1f, 0.8f, 1 }
	 * 
	 * <p>
	 * </br>The first three components represent the x,y,z component
	 * </br>The fourth component represents the type.
	 * </br>
	 * </br> 0 : Represents a directional light (Spotlight)
	 * </br> 1 : Represents a positional light (Torch) [DEFAULT]
	 * </p>
	 * 
	 */
	public final static void setPosition(float[] pos) {
		position[0] = pos[0];
		position[1] = pos[1];
		position[2] = pos[2];
		if (pos.length == 4) position[3] = pos[3];
		else position[3] = 1;
		
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		gl.glLightfv(lightId, GLLightingFunc.GL_POSITION, position, 0);
	}
	
	/**
	 * 
	 *  Points the light-source to an angle described by a THREE
	 *  dimensional array. For example: { 0.0f, 1.0f, 0.0f }
	 */
	public final static void setDirection(float[] dir) {
		direction[0] = dir[0];
		direction[1] = dir[1];
		direction[2] = dir[2];
		
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		gl.glLightfv(lightId, GLLightingFunc.GL_SPOT_DIRECTION, direction, 0);
	}
	
	/**
	 * <p>
	 * Sets the cutoff angle to a supplied angle in degrees.
	 * </p><p>
	 * Valid values are 0 to 180, values will be capped.
	 * </p><p>
	 * An angle of 0 will not be visible, an angle of 180 is the same as a positional light
	 * </p>
	 */
	public final static void setAngle(float cutoff) {
		angle = cutoff > 180 ? 180 : cutoff < 0 ? 0 : cutoff;
		
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		gl.glLightf(lightId, GLLightingFunc.GL_SPOT_CUTOFF, angle);
	}
	
	/**
	 * <p>
	 * Sets the value of the inner brightness. This is how bright the light-source appears.
	 * </p>
	 * 
	 * <p>
	 * Valid values are from 0 to 128. 
	 * </br> 0 is not bright
	 * </br> 128 is way too bright
	 * </br> 1 is default
	 * </br> Values are capped.
	 * </p>
	 */
	public final static void setBrightnessInner(float exp) {
		exponent = exp > 128 ? 128 : exp < 0 ? 0 : exp;
		
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		gl.glLightf(lightId, GLLightingFunc.GL_SPOT_EXPONENT, exponent);
	}
	
	/**
	 * Sets the constant attenuation to a given value
	 */
	public final static void setConstantAttenuation(float val) {
		constantAtt = val;
		
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		gl.glLightf(lightId, GLLightingFunc.GL_CONSTANT_ATTENUATION, constantAtt);
	}
	
	/**
	 * Sets the linear attenuation to a given value
	 * <p>
	 * Warning: This may cause a slight lag in performance
	 */
	public final static void setLinearAttenuation(float val) {
		linearAtt = val;
		
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		gl.glLightf(lightId, GLLightingFunc.GL_LINEAR_ATTENUATION, linearAtt);
	}
	
	/**
	 * Sets the quadratic attenuation to a given valu
	 * <p>
	 * Warning: This will cause a significant lag in performance
	 */
	public final static void setQuadraticAttenuation(float val) {
		quadraticAtt = val;
		
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		gl.glLightf(lightId, GLLightingFunc.GL_QUADRATIC_ATTENUATION, quadraticAtt);
	}
	
	public final static boolean isEnabled() {
		return isEnabled;
	}
	
	public final static void activate(GL2 gl) {
		gl.glEnable(GLLightingFunc.GL_LIGHTING);
    	
    	gl.glDepthFunc(GL.GL_EQUAL);
    	gl.glColorMask(true, true, true, true);
    	
    	gl.glCullFace(GL.GL_BACK);
	}
	
	public final static void deactivate(GL2 gl) {
		gl.glDisable(GLLightingFunc.GL_LIGHTING);
	}
}
