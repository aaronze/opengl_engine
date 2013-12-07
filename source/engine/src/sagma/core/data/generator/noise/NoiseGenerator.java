package sagma.core.data.generator.noise;

import sagma.core.data.generator.array.ArrayGenerator;
import sagma.core.data.generator.number.RandomNumberGenerator;

/**
 * Generates an array of noise.
 * Can generate up to 4 Dimensions of noise
 * 
 * @author Aaron Kison
 *
 */
public class NoiseGenerator extends ArrayGenerator {
	RandomNumberGenerator gen;
	int width, height, depth, rok;
	
	public NoiseGenerator(int w) {
		width = w;
		init();
	}
	
	public NoiseGenerator(int w, int h) {
		width = w;
		height = h;
		init();
	}
	
	public NoiseGenerator(int w, int h, int d) {
		width = w;
		height = h;
		depth = d;
		init();
	}
	
	public NoiseGenerator(int r, int w, int h, int d) {
		width = w;
		height = h;
		depth = d;
		rok = r;
		init();
	}
	
	private void init() {
		gen = new RandomNumberGenerator(0, 1, (int)(Math.random()*Integer.MAX_VALUE));
	}

	@Override
	public float[] nextArray1D() {
		float[] a = new float[width];
		for (int i = 0; i < width; i++) {
			a[i] = gen.nextNumber();
		}
		return a;
	}

	@Override
	public float[][] nextArray2D() {
		float[][] a = new float[width][height];
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				a[i][j] = gen.nextNumber();
			}
		}
		return a;
	}

	@Override
	public float[][][] nextArray3D() {
		float[][][] a = new float[width][height][depth];
		for (int k = 0; k < depth; k++) {
			for (int j = 0; j < height; j++) {
				for (int i = 0; i < width; i++) {
					a[i][j][k] = gen.nextNumber();
				}
			}
		}
		return a;
	}
	
	public float[][][][] nextArray4D() {
		float[][][][] a = new float[rok][width][height][depth];
		for (int r = 0; r < rok; r++) {
			for (int k = 0; k < depth; k++) {
				for (int j = 0; j < height; j++) {
					for (int i = 0; i < width; i++) {
						a[r][i][j][k] = gen.nextNumber();
					}
				}
			}
		}
		
		return a;
	}
}
