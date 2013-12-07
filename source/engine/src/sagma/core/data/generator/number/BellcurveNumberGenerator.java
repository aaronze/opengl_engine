package sagma.core.data.generator.number;

import sagma.core.data.generator.array.DefinedArrayGenerator;
import sagma.core.data.generator.selector.FastWeightedArraySelectorGenerator;
import sagma.core.data.generator.selector.SelectorGenerator;

/**
 * Generates numbers between 0 and 1 such that 0 is more likely then 1 using a function
 * of half a bellcurve.
 * 
 * Typically, the numbers between 0 and 0.5 represent about 70% of the numbers and
 * 0 to 0.25 should be about 50% of the numbers.
 * 
 * @author Aaron Kison
 *
 */
public class BellcurveNumberGenerator extends NumberGenerator {
	private SelectorGenerator bell;
	private RandomNumberGenerator step;
	
	public BellcurveNumberGenerator() {
		float[][][] f = new float[100][1][1];
		for (int i = 0; i < 100; i++) {
			f[i][0][0] = (float)(Math.pow(i/100.0f, 3));
		}
		bell = new FastWeightedArraySelectorGenerator(new DefinedArrayGenerator(f));
		step = new RandomNumberGenerator(0.0f, 0.02f);
	}

	@Override
	public float nextNumber() {
		float base = bell.nextValue();
		base += step.nextNumber() - 0.01f;
		if (base > 1) base = 1.0f;
		else if (base < 0) base = 0.0f;
		return base;
	}

}
