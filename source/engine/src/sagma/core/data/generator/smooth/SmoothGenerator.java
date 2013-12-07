package sagma.core.data.generator.smooth;

public class SmoothGenerator {
	private static float[][] v2;
	private static float[][][] v3;
	private static float[][][][] v4;
	private static int w, h, d, r;
	
	public static float[] smooth(float[] f) {
		int size = f.length;
		float[] a = new float[size];
		a[0] = f[0];
		a[size-1] = f[size-1];
		
		for (int i = 1; i < size-1; i++) {
			a[i] = (f[i-1] + f[i] + f[i+1]) / 3;
		}
		
		return a;
	}
	
	public static float[][] smooth(float[][] f) {
		w = f.length;
		h = f[0].length;
		v2 = f;
		
		float[][] a = new float[w][h];
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				float v01 = (f(x-1, y-1) + f(x+1, y-1) + f(x-1, y+1) + f(x+1, y+1)) / 16;
				float v02 = (f(x, y-1) + f(x, y+1) + f(x+1, y) + f(x-1, y)) / 8;
				float v03 = f(x, y) / 4;
				
				a[x][y] = v01 + v02 + v03;
			}
		}
		
		return a;
	}
	
	public static float[][][] smooth(float[][][] f) {
		w = f.length;
		h = f[0].length;
		d = f[0][0].length;
		v3 = f;
		
		float[][][] a = new float[w][h][d];
		
		for (int z = 0; z < d; z++) {
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					float v01 = (f(x-1, y-1, z-1) + f(x+1, y-1, z-1) + f(x-1, y+1, z-1) + f(x+1, y+1, z-1) +
							f(x-1, y-1, z+1) + f(x+1, y-1, z+1) + f(x-1, y+1, z+1) + f(x+1, y+1, z+1)) / 32;
					float v02 = (f(x, y-1, z) + f(x, y+1, z) + f(x+1, y, z) + f(x-1, y, z) + f(x, y, z-1) + f(x, y, z+1)) / 16;
					float v03 = (f(x-1, y, z-1) + f(x+1, y, z-1) + f(x, y-1, z-1) + f(x, y+1, z-1) +
							f(x-1, y-1, z) + f(x+1, y-1, z) + f(x-1, y+1, z) + f(x+1, y+1, z) +
							f(x-1, y, z+1) + f(x+1, y, z+1) + f(x, y-1, z+1) + f(x, y+1, z+1)) / 16;
					float v04 = f(x, y, z) / 4;
					
					a[x][y][z] = v01 + v02 + v03 + v04;
				}
			}
		}
		
		return a;
	}
	
	public static float[][][][] smooth(float[][][][] f) {
		r = f.length;
		w = f[0].length;
		h = f[0][0].length;
		d = f[0][0][0].length;
		v4 = f;
		
		float[][][][] a = new float[r][w][h][d];
		
		for (int v = 0; v < r; v++) {
		for (int z = 0; z < d; z++) {
		for (int y = 0; y < h; y++) {
		for (int x = 0; x < w; x++) {
			float val = 0;
			for (int wi = v-1; wi <= v+1; wi++) {
			for (int zi = z-1; zi <= z+1; zi++) {
			for (int yi = y-1; yi <= y+1; yi++) {
			for (int xi = x-1; xi <= x+1; xi++) {
				int dist = Math.abs(x-xi) + Math.abs(y-yi) + Math.abs(z-zi) + Math.abs(v-wi);
				val += f(wi, xi, yi, zi) / (dist + 1);
			}
			}
			}
			}
			a[v][x][y][z] = val / 16.0f;			
		}
		}
		}
		}
		
		return a;
	}
	
	private static float f(int x, int y) {
		if (x < 0) x += w;
		else if (x >= w) x -= w;
		if (y < 0) y += h;
		else if (y >= h) y -= h;
		return v2[x][y];
	}
	
	private static float f(int x, int y, int z) {
		if (x < 0) x += w;
		else if (x >= w) x -= w;
		if (y < 0) y += h;
		else if (y >= h) y -= h;
		if (z < 0) z += d;
		else if (z >= d) z -= d;
		return v3[x][y][z];
	}
	
	private static float f(int v, int x, int y, int z) {
		if (x < 0) x += w;
		else if (x >= w) x -= w;
		if (y < 0) y += h;
		else if (y >= h) y -= h;
		if (z < 0) z += d;
		else if (z >= d) z -= d;
		if (v < 0) v += r;
		else if (v >= r) v -= r;
		return v4[v][x][y][z];
	}
	
}
