package sagma.core.data.generator.vector;

import sagma.core.math.Vec3;

public class ConstantVectorGenerator extends VectorGenerator {
	public final static ConstantVectorGenerator ZERO = new ConstantVectorGenerator(0,0,0);
	
	private Vec3 vector;

	public ConstantVectorGenerator(Vec3 vec) {
		vector = vec;
	}
	
	public ConstantVectorGenerator(float x, float y, float z) {
		vector = new Vec3(x, y, z);
	}

	@Override
	public Vec3 nextVector(Object caller) {
		return new Vec3(vector);
	}

	@Override
	public void setNextVector(Object caller, Vec3 vector) {
		vector.set(vector);
	}
}
