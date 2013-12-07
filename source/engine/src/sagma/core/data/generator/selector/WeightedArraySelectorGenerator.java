package sagma.core.data.generator.selector;

import sagma.core.data.generator.array.ArrayGenerator;

public class WeightedArraySelectorGenerator extends ArraySelectorGenerator {
	float total;
	float[][][] weights;
	
	public WeightedArraySelectorGenerator(ArrayGenerator ag) {
		super(ag);
		
		total = 1;
		
		float avg = 1.0f / (w*h*d);
		
		weights = new float[w][h][d];
		for (int z = d-1; z >= 0; z--) {
			for (int y = h-1; y >= 0; y--) {
				for (int x = w-1; x >= 0; x--) {
					weights[x][y][z] = avg;
				}
			}
		}
	}

	@Override
	public float nextValue() {
		float v = (float)Math.random()*total;
		
		for (int z = d-1; z >= 0; z--) {
			for (int y = h-1; y >= 0; y--) {
				for (int x = w-1; x >= 0; x--) {
					v -= weights[x][y][z];
					if (v < 0) return vals[x][y][z];
				}
			}
		}
		
		return 0;
	}

}
