package sagma.core.data.generator.vector;

import sagma.core.math.Vec3;

public abstract class VectorGenerator {
	/**
	 * @deprecated
	 */
	public Vec3 nextVector(Object caller) {
		Vec3 v = new Vec3(0,0,0);
		setNextVector(caller, v);
		return v;
	}
	public abstract void setNextVector(Object caller, Vec3 vector);
}
