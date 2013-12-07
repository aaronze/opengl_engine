package sagma.core.data.generator.vector;

import sagma.core.data.generator.number.ConstantNumberGenerator;
import sagma.core.data.generator.number.NumberGenerator;
import sagma.core.math.Vec3;

public class VariableVectorGenerator extends VectorGenerator {
	private NumberGenerator x;
	private NumberGenerator y;
	private NumberGenerator z;
	
	public VariableVectorGenerator(float x, float y, float z) {
		this.x = new ConstantNumberGenerator(x);
		this.y = new ConstantNumberGenerator(y);
		this.z = new ConstantNumberGenerator(z);
	}
	
	public VariableVectorGenerator(NumberGenerator x, float y, float z) {
		this.x = x;
		this.y = new ConstantNumberGenerator(y);
		this.z = new ConstantNumberGenerator(z);
	}
	
	public VariableVectorGenerator(float x, NumberGenerator y, float z) {
		this.x = new ConstantNumberGenerator(x);
		this.y = y;
		this.z = new ConstantNumberGenerator(z);
	}
	
	public VariableVectorGenerator(float x, float y, NumberGenerator z) {
		this.x = new ConstantNumberGenerator(x);
		this.y = new ConstantNumberGenerator(y);
		this.z = z;
	}
	
	public VariableVectorGenerator(float x, NumberGenerator y, NumberGenerator z) {
		this.x = new ConstantNumberGenerator(x);
		this.y = y;
		this.z = z;
	}
	
	public VariableVectorGenerator(NumberGenerator x, float y, NumberGenerator z) {
		this.x = x;
		this.y = new ConstantNumberGenerator(y);
		this.z = z;
	}
	
	public VariableVectorGenerator(NumberGenerator x, NumberGenerator y, float z) {
		this.x = x;
		this.y = y;
		this.z = new ConstantNumberGenerator(z);
	}
	
	public VariableVectorGenerator(NumberGenerator x, NumberGenerator y, NumberGenerator z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
