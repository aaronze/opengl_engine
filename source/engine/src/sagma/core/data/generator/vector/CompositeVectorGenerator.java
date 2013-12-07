package sagma.core.data.generator.vector;

import sagma.core.math.Vec3;

public class CompositeVectorGenerator extends VectorGenerator {
	private VectorGenerator v1;
	private VectorGenerator v2;
	private Vec3 a = new Vec3(0,0,0);
	private Vec3 b = new Vec3(0,0,0);
	
	public CompositeVectorGenerator(VectorGenerator a, VectorGenerator b) {
		v1 = a;
		v2 = b;
	}

	@Override
	public void setNextVector(Object caller, Vec3 vector) {
		v1.setNextVector(caller, a);
		v2.setNextVector(caller, b);
		a.m_add(b);
		vector.set(a);
	}
}
