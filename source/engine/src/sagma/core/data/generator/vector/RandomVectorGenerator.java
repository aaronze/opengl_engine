package sagma.core.data.generator.vector;

import sagma.core.data.generator.number.NumberGenerator;
import sagma.core.data.generator.number.RandomNumberGenerator;
import sagma.core.math.Vec3;

public class RandomVectorGenerator extends VectorGenerator {
	private NumberGenerator x;
	private NumberGenerator y;
	private NumberGenerator z;
	
	public RandomVectorGenerator() {
		x = new RandomNumberGenerator();
		y = new RandomNumberGenerator();
		z = new RandomNumberGenerator();
	}
	
	public RandomVectorGenerator(float maxX, float maxY, float maxZ) {
		x = new RandomNumberGenerator(maxX);
		y = new RandomNumberGenerator(maxY);
		z = new RandomNumberGenerator(maxZ);
	}
	
	public RandomVectorGenerator(float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
		x = new RandomNumberGenerator(minX, maxX);
		y = new RandomNumberGenerator(minY, maxY);
		z = new RandomNumberGenerator(minZ, maxZ);
	}
	
	@Override
	public Vec3 nextVector(Object caller) {
		return new Vec3(x.nextNumber(), y.nextNumber(), z.nextNumber());
	}

	@Override
	public void setNextVector(Object caller, Vec3 vector) {
		vector.set(x.nextNumber(), y.nextNumber(), z.nextNumber());
	}

}
