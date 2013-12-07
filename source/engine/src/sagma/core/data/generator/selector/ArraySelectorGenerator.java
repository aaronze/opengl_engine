package sagma.core.data.generator.selector;

import sagma.core.data.generator.array.ArrayGenerator;

public class ArraySelectorGenerator extends SelectorGenerator {
	int w, h, d;
	float[][][] vals;
	
	public ArraySelectorGenerator(ArrayGenerator ag) {
		vals = ag.nextArray3D();
		w = vals.length;
		h = vals[0].length;
		d = vals[0][0].length;
	}
	
	@Override
	public float nextValue() {
		int x = (int)(Math.random()*w);
		int y = (int)(Math.random()*h);
		int z = (int)(Math.random()*d);
		
		return vals[x][y][z];
	}
}
