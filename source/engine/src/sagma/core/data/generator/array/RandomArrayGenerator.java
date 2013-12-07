package sagma.core.data.generator.array;

import sagma.core.data.generator.number.NumberGenerator;
import sagma.core.data.generator.number.RandomNumberGenerator;

/**
 * Generates arrays with the cells generating a new random number
 * every generation
 * 
 * @author Aaron Kison
 *
 */
public class RandomArrayGenerator extends ArrayGenerator {
	private NumberGenerator number;
	
	{
		width = 1;
		height = 1;
		depth = 1;
	}
	
	public RandomArrayGenerator(NumberGenerator gen) {
		number = gen;
	}
	
	public RandomArrayGenerator(NumberGenerator gen, int w) {
		number = gen;
		width = w;
	}
	
	public RandomArrayGenerator(NumberGenerator gen, int w, int h) {
		number = gen;
		width = w;
		height = h;
	}
	
	public RandomArrayGenerator(NumberGenerator gen, int w, int h, int d) {
		number = gen;
		width = w;
		height = h;
		depth = d;
	}
	
	public RandomArrayGenerator(float max) {
		number = new RandomNumberGenerator(max);
	}
	
	public RandomArrayGenerator(float max, int w) {
		number = new RandomNumberGenerator(max);
		width = w;
	}
	
	public RandomArrayGenerator(float max, int w, int h) {
		number = new RandomNumberGenerator(max);
		width = w;
		height = h;
	}
	
	public RandomArrayGenerator(float max, int w, int h, int d) {
		number = new RandomNumberGenerator(max);
		width = w;
		height = h;
		depth = d;
	}
	
	public RandomArrayGenerator(float min, float max) {
		number = new RandomNumberGenerator(min, max);
	}
	
	public RandomArrayGenerator(float min, float max, int w) {
		number = new RandomNumberGenerator(min, max);
		width = w;
	}
	
	public RandomArrayGenerator(float min, float max, int w, int h) {
		number = new RandomNumberGenerator(min, max);
		width = w;
		height = h;
	}
	
	public RandomArrayGenerator(float min, float max, int w, int h, int d) {
		number = new RandomNumberGenerator(min, max);
		width = w;
		height = h;
		depth = d;
	}

	@Override
	public float[] nextArray1D() {
		float[] array = new float[width];
		for (int i = width-1; i >= 0; i--) {
			array[i] = number.nextNumber();
		}
		return array;
	}

	@Override
	public float[][] nextArray2D() {
		float[][] array = new float[width][height];
		for (int y = height-1; y >= 0; y--) {
			for (int x = width-1; x >= 0; x--) {
				array[x][y] = number.nextNumber();
			}
		}
		return array;
	}

	@Override
	public float[][][] nextArray3D() {
		float[][][] array = new float[width][height][depth];
		for (int z = depth-1; z >= 0; z--) {
			for (int y = height-1; y >= 0; y--) {
				for (int x = width-1; x >= 0; x--) {
					array[x][y][z] = number.nextNumber();
				}
			}
		}
		return array;
	}
}
