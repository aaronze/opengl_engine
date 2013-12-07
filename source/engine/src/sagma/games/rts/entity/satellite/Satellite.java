package sagma.games.rts.entity.satellite;

import sagma.games.rts.emitters.Entity;
import sagma.core.math.Angle;
import sagma.core.math.Math;
import sagma.core.math.Vec3;

/**
 * 
 * @author Sam
 *
 */

public class Satellite extends Entity{

	public float orbitSpeed;
	public float orbitRadius;
	public Angle angle;
	public Vec3 parentLocation;
	
	/**
	 * 
	 * @param modelName
	 * @param orbitCenter
	 * @param orbitRadius
	 * @param orbitSpeed
	 * @param eccentricity
	 */
	public Satellite(String modelName, Vec3 orbitCenter, float orbitRadius, float orbitSpeed) {
		super(modelName);
		this.parentLocation = orbitCenter;
		this.orbitRadius = orbitRadius;
		this.orbitSpeed = orbitSpeed;
		
		angle = new Angle(0f);
	}
	
	@Override
	public void heartbeat() {
		angle.set(angle.angleValue()+orbitSpeed);
		
		setPosition((Math.cos(angle.angleValue())*orbitRadius)+parentLocation.getX(), 
				(Math.sin(angle.angleValue())*orbitRadius)+parentLocation.getY(), 
				getPosition().z);
	}
}
