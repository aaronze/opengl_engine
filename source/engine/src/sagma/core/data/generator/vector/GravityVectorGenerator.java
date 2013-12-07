package sagma.core.data.generator.vector;

import sagma.core.data.generator.number.NumberGenerator;
import sagma.core.math.Vec3;
import sagma.core.particle.Particle;

public class GravityVectorGenerator extends VectorGenerator {
	private VectorGenerator point;
	private NumberGenerator force;
	private static Vec3 v = new Vec3(0,0,0);
	
	public GravityVectorGenerator(VectorGenerator gravityPoint, NumberGenerator gravitySize) {
		point = gravityPoint;
		force = gravitySize;
	}
	
	public GravityVectorGenerator() {}

	@Override
	public Vec3 nextVector(Object caller) {
		point.setNextVector(caller, v);
		if (caller.getClass().equals(Particle.class)) {
			Particle p = (Particle)caller;
			Vec3 pos = p.state().getPosition();
			v.m_subtract(pos);
			v.m_normalize();
			v.m_scale(force.nextNumber());
		}
		return v;
	}

	@Override
	public void setNextVector(Object caller, Vec3 vector) {
		vector.set(nextVector(caller));
	}
	

}
