package sagma.core.data.generator.array;

import sagma.core.data.generator.number.NumberGenerator;

/**
 * Generates arrays with every cell the same given constant value.
 * 
 * If undefined the default width, height and depth are all 1.
 * 
 * @author Aaron Kison
 *
 */
public class ConstantArrayGenerator extends ArrayGenerator {
	private float number;
	
	public ConstantArrayGenerator(float f) {
		number = f;
	}
	
	public ConstantArrayGenerator(float f, int w) {
		number = f;
		width = w;
	}
	
	public ConstantArrayGenerator(float f, int w, int h) {
		number = f;
		width = w;
		height = h;
	}
	
	public ConstantArrayGenerator(float f, int w, int h, int d) {
		number = f;
		width = w;
		height = h;
		depth = d;
	}
	
	public ConstantArrayGenerator(NumberGenerator gen) {
		number = gen.nextNumber();
	}
	
	public ConstantArrayGenerator(NumberGenerator gen, int w) {
		number = gen.nextNumber();
		width = w;
	}
	
	public ConstantArrayGenerator(NumberGenerator gen, int w, int h) {
		number = gen.nextNumber();
		width = w;
		height = h;
	}
	
	public ConstantArrayGenerator(NumberGenerator gen, int w, int h, int d) {
		number = gen.nextNumber();
		width = w;
		height = h;
		depth = d;
	}

	@Override
	public float[] nextArray1D() {
		float[] array = new float[width];
		for (int i = width-1; i >= 0; i--) {
			array[i] = number;
		}
		return array;
	}

	@Override
	public float[][] nextArray2D() {
		float[][] array = new float[width][height];
		for (int y = height-1; y >= 0; y--) {
			for (int x = width-1; x >= 0; x--) {
				array[x][y] = number;
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
					array[x][y][z] = number;
				}
			}
		}
		return array;
	}
	
	
}
