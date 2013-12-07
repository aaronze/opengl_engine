package sagma.core.data.generator.color;

import sagma.core.data.Color4f;
import sagma.core.data.generator.number.ConstantNumberGenerator;

/**
 * Generates a colour by replicating the given original.
 * The original is never modified.
 * 
 * @author Aaron Kison
 *
 */
public class ConstantColorGenerator extends ColorGenerator {

	public ConstantColorGenerator(Color4f color) {
		super(new ConstantNumberGenerator(color.getRed()),
				new ConstantNumberGenerator(color.getGreen()),
				new ConstantNumberGenerator(color.getBlue()));
	}

}
