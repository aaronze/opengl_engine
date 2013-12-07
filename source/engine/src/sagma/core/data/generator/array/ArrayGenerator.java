package sagma.core.data.generator.array;

/**
 * Generates a multi-dimensional array.
 * If single-dimension use is wanted and can't be guaranteed then the array
 * should be wrapped into a higher dimension.
 * 
 * @author Aaron Kison
 *
 */
public abstract class ArrayGenerator {
	int width = 1;
	int height = 1;
	int depth = 1;
	
	public abstract float[] nextArray1D();
	public abstract float[][] nextArray2D();
	public abstract float[][][] nextArray3D();
}
