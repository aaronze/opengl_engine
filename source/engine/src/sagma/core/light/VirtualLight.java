package sagma.core.light;

import sagma.core.data.Color4f;
import sagma.core.math.Vec3;
import sagma.core.render.Game;

public class VirtualLight {
	public final static int POINT_LIGHT = 0;
	public final static int DIRECTION_LIGHT = 1;
	
	private boolean isEnabled = true;
	private Color4f diffuseColor = Color4f.WHITE;
	private Color4f specularColor = Color4f.WHITE;
	private Color4f ambientColor = Color4f.BLACK;
	private float[] direction = new float[]{0,0,0,0};
	private float[] position = new float[]{0,0,1,POINT_LIGHT};
	private float angle = 180.0f;
	private float exponent = 0.0f;
	private float constantAtt = 1.0f;
	private float linearAtt = 0.0f;
	private float quadAtt = 0.0f;
	
	public Vec3 getPosition() {
		return new Vec3(position[0], position[1], position[2]);
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void enable() {
		isEnabled = true;
	}
	
	public void disable() {
		isEnabled = false;
	}
	
	public void setPosition(Vec3 v) {
		position[0] = v.x;
		position[1] = v.y;
		position[2] = v.z;
	}
	
	public void useLight(int i) {
		if (i == 0) useLight0();
		else if (i == 1) useLight1();
		else if (i == 2) useLight2();
		else if (i == 3) useLight3();
		else if (i == 4) useLight4();
		else if (i == 5) useLight5();
		else if (i == 6) useLight6();
		else useLight7();
	}
	
	public void useLight0() {
		Light0.setEnabled(true);
		Light0.setDiffuseColor(diffuseColor);
		Light0.setAmbientColor(ambientColor);
		Light0.setSpecularColor(specularColor);
		Light0.setPosition(position);
		Light0.setDirection(direction);
		Light0.setAngle(angle);
		Light0.setBrightnessInner(exponent);
		Light0.setConstantAttenuation(constantAtt);
		Light0.setLinearAttenuation(linearAtt);
		Light0.setQuadraticAttenuation(quadAtt);
		Light0.activate(Game.savedDrawable.getGL().getGL2());
	}
	public void useLight1() {
		Light1.setEnabled(true);
		Light1.setDiffuseColor(diffuseColor);
		Light1.setAmbientColor(ambientColor);
		Light1.setSpecularColor(specularColor);
		Light1.setPosition(position);
		Light1.setDirection(direction);
		Light1.setAngle(angle);
		Light1.setBrightnessInner(exponent);
		Light1.setConstantAttenuation(constantAtt);
		Light1.setLinearAttenuation(linearAtt);
		Light1.setQuadraticAttenuation(quadAtt);
		Light1.activate(Game.savedDrawable.getGL().getGL2());
	}
	public void useLight2() {
		Light2.setEnabled(true);
		Light2.setDiffuseColor(diffuseColor);
		Light2.setAmbientColor(ambientColor);
		Light2.setSpecularColor(specularColor);
		Light2.setPosition(position);
		Light2.setDirection(direction);
		Light2.setAngle(angle);
		Light2.setBrightnessInner(exponent);
		Light2.setConstantAttenuation(constantAtt);
		Light2.setLinearAttenuation(linearAtt);
		Light2.setQuadraticAttenuation(quadAtt);
		Light2.activate(Game.savedDrawable.getGL().getGL2());
	}
	public void useLight3() {
		Light3.setEnabled(true);
		Light3.setDiffuseColor(diffuseColor);
		Light3.setAmbientColor(ambientColor);
		Light3.setSpecularColor(specularColor);
		Light3.setPosition(position);
		Light3.setDirection(direction);
		Light3.setAngle(angle);
		Light3.setBrightnessInner(exponent);
		Light3.setConstantAttenuation(constantAtt);
		Light3.setLinearAttenuation(linearAtt);
		Light3.setQuadraticAttenuation(quadAtt);
		Light3.activate(Game.savedDrawable.getGL().getGL2());
	}
	public void useLight4() {
		Light4.setEnabled(true);
		Light4.setDiffuseColor(diffuseColor);
		Light4.setAmbientColor(ambientColor);
		Light4.setSpecularColor(specularColor);
		Light4.setPosition(position);
		Light4.setDirection(direction);
		Light4.setAngle(angle);
		Light4.setBrightnessInner(exponent);
		Light4.setConstantAttenuation(constantAtt);
		Light4.setLinearAttenuation(linearAtt);
		Light4.setQuadraticAttenuation(quadAtt);
		Light4.activate(Game.savedDrawable.getGL().getGL2());
	}
	public void useLight5() {
		Light5.setEnabled(true);
		Light5.setDiffuseColor(diffuseColor);
		Light5.setAmbientColor(ambientColor);
		Light5.setSpecularColor(specularColor);
		Light5.setPosition(position);
		Light5.setDirection(direction);
		Light5.setAngle(angle);
		Light5.setBrightnessInner(exponent);
		Light5.setConstantAttenuation(constantAtt);
		Light5.setLinearAttenuation(linearAtt);
		Light5.setQuadraticAttenuation(quadAtt);
		Light5.activate(Game.savedDrawable.getGL().getGL2());
	}
	public void useLight6() {
		Light6.setEnabled(true);
		Light6.setDiffuseColor(diffuseColor);
		Light6.setAmbientColor(ambientColor);
		Light6.setSpecularColor(specularColor);
		Light6.setPosition(position);
		Light6.setDirection(direction);
		Light6.setAngle(angle);
		Light6.setBrightnessInner(exponent);
		Light6.setConstantAttenuation(constantAtt);
		Light6.setLinearAttenuation(linearAtt);
		Light6.setQuadraticAttenuation(quadAtt);
		Light6.activate(Game.savedDrawable.getGL().getGL2());
	}
	public void useLight7() {
		Light7.setEnabled(true);
		Light7.setDiffuseColor(diffuseColor);
		Light7.setAmbientColor(ambientColor);
		Light7.setSpecularColor(specularColor);
		Light7.setPosition(position);
		Light7.setDirection(direction);
		Light7.setAngle(angle);
		Light7.setBrightnessInner(exponent);
		Light7.setConstantAttenuation(constantAtt);
		Light7.setLinearAttenuation(linearAtt);
		Light7.setQuadraticAttenuation(quadAtt);
		Light7.activate(Game.savedDrawable.getGL().getGL2());
	}
	
	public final void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}
	
	public final void setDiffuseColor(Color4f color) {
		diffuseColor = color;
	}
	
	public final void setAmbientColor(Color4f color) {
		ambientColor = color;
	}
	
	public final void setSpecularColor(Color4f color) {
		specularColor = color;
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
	public final void setPosition(float[] pos) {
		position[0] = pos[0];
		position[1] = pos[1];
		position[2] = pos[2];
		position[3] = pos[3];
	}
	
	/**
	 * 
	 *  Points the light-source to an angle described by a THREE
	 *  dimensional array. For example: { 0.0f, 1.0f, 0.0f }
	 */
	public final void setDirection(float[] dir) {
		direction[0] = dir[0];
		direction[1] = dir[1];
		direction[2] = dir[2];
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
	public final void setAngle(float cutoff) {
		angle = cutoff > 180 ? 180 : cutoff < 0 ? 0 : cutoff;
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
	public final void setBrightnessInner(float exp) {
		exponent = exp > 128 ? 128 : exp < 0 ? 0 : exp;
	}
	
	/**
	 * Sets the constant attenuation to a given value
	 */
	public final void setConstantAttenuation(float val) {
		constantAtt = val;
	}
	
	public final void setLinearAttenuation(float val) {
		linearAtt = val;
	}
	
	public final void setQuadraticAttenuation(float val) {
		quadAtt = val;
	}
}
