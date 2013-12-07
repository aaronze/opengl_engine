package sagma.core.data.generator.fractal;

public abstract class FractalGenerator {
	/**
	 * <p>
	 * Generates and returns the next sequence of the array.
	 * </p>
	 * 
	 * <p>
	 * It is important to note that the returned array will always be larger then
	 * the given array. Also each fractal generator will return a different
	 * size then the original and from each other. Instead of trying to
	 * statically calculate the returned size, the size can be found by using
	 * the following:
	 * </p>
	 * 
	 * <p><ul><code>
	 * <li>int width = array.length;
	 * <li>int height = array[0].length;
	 * </code></ul></p>
	 * 
	 * <p>
	 * It is also important to note that any real work with fractals would
	 * require multiple iterative calls to the nextFractal. For example one could
	 * use:
	 * </p>
	 * <p><code>
	 * float[][] endResult = nextFractal(nextFractal(nextFractal( array ));
	 * </code></p>
	 * 
	 * <p><code>
	 * </br>Or:
	 * </br>float[][] array;
	 * </br>for (int i = 0; i < 3; i++) {
	 * </br>	array = nextFractal(array);
	 * </br>}
	 * </code></p>
	 * 
	 * <p>
	 * These will both iteratively produce a 3rd generation fractal.
	 * </p>
	 * 
	 * @param f Initial state of the array
	 * @return Next Fractal array in the series, generally of size (width*2, height*2).
	 */
	public abstract float[][] nextFractal(float[][] f);
}
