package sagma.core.data.generator.noise;

import sagma.core.data.generator.smooth.SmoothGenerator;
import sagma.core.math.Interpolation;

/**
 * Generates an array of Perlin Noise. Perlin noise is multiple noise functions
 * layered at differing frequencies.
 * 
 * @author Aaron Kison
 *
 */
public class PerlinNoiseGenerator extends NoiseGenerator {
	public float FALLOFF = 0.3f;
	public float SHARPNESS = 0.9f;
	
	public PerlinNoiseGenerator(int w) {
		super(w);
	}
	
	public PerlinNoiseGenerator(int w, int h) {
		super(w, h);
	}
	
	public PerlinNoiseGenerator(int w, int h, int d) {
		super(w, h, d);
	}
	
	public PerlinNoiseGenerator(int w, int h, int d, int r) {
		super(w, h, d, r);
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
		int s3 = width>>1;
		int s2 = width>>2;
		int s1 = width>>3;
		
		NoiseGenerator n32 = new NoiseGenerator(s1, s1);
		
		float[][] a32 = SmoothGenerator.smooth(n32.nextArray2D());
		float[][] b32 = SmoothGenerator.smooth(n32.nextArray2D());
		float[][] c32 = SmoothGenerator.smooth(n32.nextArray2D());
		float[][] d32 = SmoothGenerator.smooth(n32.nextArray2D());
		
		float[][] b64 = scale(b32, 2);
		float[][] c128 = scale(c32, 4);
		float[][] d256 = scale(d32, 8);
		
		float[][] noise = new float[width][height];
		float max = -1000;
		float min = 10000;
		for (int y = height-1; y >= 0; y--) {
			for (int x = width-1; x >= 0; x--) {
				float a = a32[x%s1][y%s1] / 8;
				float b = b64[x%s2][y%s2] / 4;
				float c = c128[x%s3][y%s3] / 2;
				float d = d256[x][y];
				
				float val = (a + b + c + d);
				noise[x][y] = val;
				
				if (val > max) max = val;
				if (val < min) min = val;
			}
		}
		
		float range = max-min;
		for (int y = height-1; y >= 0; y--) {
			for (int x = width-1; x >= 0; x--) {
				float f = (noise[x][y] - min) / range;
				noise[x][y] = cloudCover(f, FALLOFF, SHARPNESS);
			}
		}
		
		return SmoothGenerator.smooth(noise);
	}

	@Override
	public float[][][] nextArray3D() {
		int s3x = width>>1;
		int s2x = width>>2;
		int s1x = width>>3;
		
		int s3y = height>>1;
		int s2y = height>>2;
		int s1y = height>>3;
		
		int s3z = depth>>1;
		int s2z = depth>>2;
		int s1z = depth>>3;
		
		NoiseGenerator n32 = new NoiseGenerator(s1x, s1y, s1z);
		
		float[][][] a32 = SmoothGenerator.smooth(n32.nextArray3D());
		float[][][] b32 = SmoothGenerator.smooth(n32.nextArray3D());
		float[][][] c32 = SmoothGenerator.smooth(n32.nextArray3D());
		float[][][] d32 = SmoothGenerator.smooth(n32.nextArray3D());
		
		float[][][] b64 = scale(b32, 2);
		float[][][] c128 = scale(c32, 4);
		float[][][] d256 = scale(d32, 8);
		
		float[][][] noise = new float[width][height][depth];
		float max = -1000;
		float min = 10000;
		for (int z = depth-1; z >= 0; z--) {
			for (int y = height-1; y >= 0; y--) {
				for (int x = width-1; x >= 0; x--) {
					float a = a32[x%s1x][y%s1y][z%s1z] / 8;
					float b = b64[x%s2x][y%s2y][z%s2z] / 4;
					float c = c128[x%s3x][y%s3y][z%s3z] / 2;
					float d = d256[x][y][z];
					
					float val = (a + b + c + d);
					noise[x][y][z] = val;
					
					if (val > max) max = val;
					if (val < min) min = val;
				}
			}
		}
		
		float range = max-min;
		for (int z = depth-1; z >= 0; z--) {
			for (int y = height-1; y >= 0; y--) {
				for (int x = width-1; x >= 0; x--) {
					float f = (noise[x][y][z] - min) / range;
					noise[x][y][z] = cloudCover(f, FALLOFF, SHARPNESS);
				}
			}
		}
		
		return SmoothGenerator.smooth(noise);
	}
	
	private static float cloudCover(float f, float cloud, float sharpness) {
		float c = f-cloud;
		if (c < 0) c = 0;
		
		float d = 1.0f - (float)(Math.pow(sharpness, c));
		return d;
	}
	
	private static float[][] scale(float[][] f, int size) {
		int w = f.length;
		int h = f[0].length;
		
		int w2 = w*size;
		int h2 = h*size;
		
		float[][] a = new float[w2][h2];
		for (int y = h2-1; y >= 0; y--) {
			for (int x = w2-1; x >= 0; x--) {
				int xi = x/size;
				int yi = y/size;
				
				float vX = (x*1.0f/size) - (x/size);
				
				float vA = f[xi][yi];
				float vB = vA;
				if (xi+1 < w) {
					vB = f[xi+1][yi];
				}
				
				a[x][y] = Interpolation.cosineInterpolate(vA, vB, vX);
			}
		}
		
		return a;
	}
	
	private static float[][][] scale(float[][][] f, int size) {
		int w = f.length;
		int h = f[0].length;
		int d = f[0][0].length;
		
		int w2 = w*size;
		int h2 = h*size;
		int d2 = d*size;
		
		float[][][] a = new float[w2][h2][d2];
		for (int z = d2-1; z >= 0; z--) {
			for (int y = h2-1; y >= 0; y--) {
				for (int x = w2-1; x >= 0; x--) {
					int xi = x/size;
					int yi = y/size;
					int zi = z/size;
					
					float vX = (x*1.0f/size) - (x/size);
					
					float vA = f[xi][yi][zi];
					float vB = vA;
					if (xi+1 < w) {
						vB = f[xi+1][yi][zi];
					}
					
					a[x][y][z] = Interpolation.cosineInterpolate(vA, vB, vX);
				}
			}
		}
		
		return a;
	}
	
	@Override
	public float[][][][] nextArray4D() {
		int s3x = width>>1;
		int s2x = width>>2;
		int s1x = width>>3;
		
		int s3y = height>>1;
		int s2y = height>>2;
		int s1y = height>>3;
		
		int s3z = depth>>1;
		int s2z = depth>>2;
		int s1z = depth>>3;
		
		int s3r = rok>>1;
		int s2r = rok>>2;
		int s1r = rok>>3;
		
		NoiseGenerator n32 = new NoiseGenerator(s1r, s1x, s1y, s1z);
		
		float[][][][] a32 = SmoothGenerator.smooth(n32.nextArray4D());
		float[][][][] b32 = SmoothGenerator.smooth(n32.nextArray4D());
		float[][][][] c32 = SmoothGenerator.smooth(n32.nextArray4D());
		float[][][][] d32 = SmoothGenerator.smooth(n32.nextArray4D());
		
		float[][][][] b64 = scale(b32, 2);
		float[][][][] c128 = scale(c32, 4);
		float[][][][] d256 = scale(d32, 8);
		
		float[][][][] noise = new float[rok][width][height][depth];
		float max = -1000;
		float min = 10000;
		for (int v = rok-1; v >= 0; v--) {
			for (int z = depth-1; z >= 0; z--) {
				for (int y = height-1; y >= 0; y--) {
					for (int x = width-1; x >= 0; x--) {
						float a = a32[v%s1r][x%s1x][y%s1y][z%s1z] / 8;
						float b = b64[v%s2r][x%s2x][y%s2y][z%s2z] / 4;
						float c = c128[v%s3r][x%s3x][y%s3y][z%s3z] / 2;
						float d = d256[v][x][y][z];
						
						float val = (a + b + c + d);
						noise[v][x][y][z] = val;
						
						if (val > max) max = val;
						if (val < min) min = val;
					}
				}
			}
		}
		
		float range = max-min;
		for (int v = rok-1; v >= 0; v--) {
			for (int z = depth-1; z >= 0; z--) {
				for (int y = height-1; y >= 0; y--) {
					for (int x = width-1; x >= 0; x--) {
						float f = (noise[v][x][y][z] - min) / range;
						noise[v][x][y][z] = cloudCover(f, FALLOFF, SHARPNESS);
					}
				}
			}
		}
		
		return SmoothGenerator.smooth(noise);
	}
	
	private static float[][][][] scale(float[][][][] f, int size) {
		int r = f.length;
		int w = f[0].length;
		int h = f[0][0].length;
		int d = f[0][0][0].length;
		
		int w2 = w*size;
		int h2 = h*size;
		int d2 = d*size;
		int r2 = r*size;
		
		float[][][][] a = new float[r2][w2][h2][d2];
		for (int v = r2-1; v >= 0; v--) {
			for (int z = d2-1; z >= 0; z--) {
				for (int y = h2-1; y >= 0; y--) {
					for (int x = w2-1; x >= 0; x--) {
						int xi = x/size;
						int yi = y/size;
						int zi = z/size;
						int vi = v/size;
						
						float vX = (x*1.0f/size) - (x/size);
						
						float vA = f[vi][xi][yi][zi];
						float vB = vA;
						if (xi+1 < w) {
							vB = f[vi][xi+1][yi][zi];
						}
						
						a[v][x][y][z] = Interpolation.cosineInterpolate(vA, vB, vX);
					}
				}
			}
		}
		
		return a;
	}
	
}
