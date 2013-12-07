package sagma.core.model.bounding;

import sagma.core.math.Vec3;

/**
 * Replaced with BoundingSphere
 * 
 * @author Aaron
 *
 */
@Deprecated
public abstract class Bounding {
	public abstract boolean collidesWith(Bounding bound, Vec3 posA, Vec3 posB);
	public abstract void instanceGotScaled(float f);
}
