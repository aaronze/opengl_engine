package sagma.core.data.generator.number;

/**
 * Generates the same given number every time it's called
 * 
 * @see NumberGenerator
 * 
 * @author Aaron Kison
 *
 */
public class ConstantNumberGenerator extends NumberGenerator {
	private float number;
	public ConstantNumberGenerator(float d) {
		number = d;
	}
	
	@Override
	public float nextNumber() {
		return number;
	}

}
