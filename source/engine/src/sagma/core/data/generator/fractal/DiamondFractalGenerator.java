package sagma.core.data.generator.fractal;

public class DiamondFractalGenerator extends FractalGenerator {
	public float H = 1.0f;
	public float INIT_H = 1.0f;

	@Override
	public float[][] nextFractal(float[][] f) {
		int w1 = f.length;
		int h1 = f[0].length;
		int w2 = w1 << 1;
		int h2 = h1 << 1;
		
		float[][] a = new float[w2][h2];
		float h = INIT_H;
		
		// Diamond step
		for (int y = h1-1; y >= 0; y--) {
			for (int x = w1-1; x >= 0; x--) {
				// Select a square down at to the right. If square
				// goes over boundry then wrap
				
				float vUL = a[x<<1][y<<1] = f[x][y];
				float vUR = 0;
				float vDL = 0;
				float vDR = 0;
				
				boolean xWraps = x == w1-1;
				boolean yWraps = y == h1-1;
				
				if (xWraps && yWraps) {
					vUR = a[0][y<<1] = f[0][y];
					vDL = a[x<<1][0] = f[x][0];
					vDR = a[0][0] = f[0][0];
				} else if (xWraps) {
					vUR = a[0][y<<1] = f[0][y];
					vDL = a[x<<1][(y+1)<<1] = f[x][y+1];
					vDR = a[0][(y+1)<<1] = f[0][y+1];
				} else if (yWraps) {
					vUR = a[(x+1)<<1][y<<1] = f[x+1][y];
					vDL = a[x<<1][0] = f[x][0];
					vDR = a[(x+1)<<1][0] = f[x+1][0];
				} else {
					vUR = a[(x+1)<<1][y<<1] = f[x+1][y];
					vDL = a[x<<1][(y+1)<<1] = f[x][y+1];
					vDR = a[(x+1)<<1][(y+1)<<1] = f[x+1][y+1];
				}
				
				float U = a[(x<<1)+1][y] = (vUL + vUR) * 0.5f;
				float L = a[x][(y<<1)+1] = (vUL + vDL) * 0.5f;
				float R = 0;
				float D = 0;
				
				if (xWraps) R = a[0][(y<<1)+1] = (vUR + vDR) * 0.5f;
				else		R = a[(x+1)<<1][(y<<1)+1] = (vUR + vDR) * 0.5f;
				
				if (yWraps) D = a[(x<<1)+1][0] = (vDL + vDR) * 0.5f;
				else 		D = a[(x<<1)+1][(y+1)<<1] = (vDL + vDR) * 0.5f;
				
				// Square step 
				float avg = (U + R + D + L) * 0.5f;
				float ran = (float)Math.random()*h;
				a[(x<<1)+1][(y<<1)+1] = avg + ran;
				
				// Reduce random interval range
				h = h / (H + 1);
			}
		}
		
		return a;
	}

}
