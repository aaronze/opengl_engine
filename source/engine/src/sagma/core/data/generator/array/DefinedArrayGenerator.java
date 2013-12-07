package sagma.core.data.generator.array;

/**
 * Generates arrays using replication of a given original.
 * 
 * If a generated array is modified then the array generated for future use
 * is also modified.
 * 
 * @author Aaron Kison
 *
 */
public class DefinedArrayGenerator extends ArrayGenerator {
	private float[] a;
	private float[][] b;
	private float[][][] c;
	
	public DefinedArrayGenerator(float[] f) {
		a = f;
	}
	
	public DefinedArrayGenerator(float[][] f) {
		b = f;
	}
	
	public DefinedArrayGenerator(float[][][] f) {
		c = f;
	}

	@Override
	public float[] nextArray1D() {
		return a;
	}

	@Override
	public float[][] nextArray2D() {
		return b;
	}

	@Override
	public float[][][] nextArray3D() {
		return c;
	}
}
