package sagma.core.data.generator.selector;

import sagma.core.data.generator.array.ArrayGenerator;
import sagma.core.data.generator.number.RandomNumberGenerator;

public class FastWeightedArraySelectorGenerator extends WeightedArraySelectorGenerator {
	float[] rangeVal;
	float[] rangeWeight;
	int size;
	private final RandomNumberGenerator ran = new RandomNumberGenerator();

	public FastWeightedArraySelectorGenerator(ArrayGenerator ag) {
		super(ag);
		
		size = (int)Math.sqrt(w*h*d);
		rangeVal = new float[size];
		rangeWeight = new float[size];
	}
	
	@Override
	public float nextValue() {
		if (size < 80) {
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
		
		total = 0;
		int x = 0, y = 0, z = 0;
		
		for (int i = size-1; i >= 0; i--) {
			if (w != 1) x = (int)(ran.nextNumber()*w);
			if (h != 1) y = (int)(ran.nextNumber()*h);
			if (d != 1) z = (int)(ran.nextNumber()*d);
			
			float weight = weights[x][y][z];
			rangeVal[i] = vals[x][y][z];
			rangeWeight[i] = weight;
			total += weight;
		}
		
		
		float v = (ran.nextNumber()*total);
		for (int i = size-1; i >= 0; i--) {
			v -= rangeWeight[i];
			if (v < 0) return rangeVal[i];
		}
		
		return 0;
	}

}
