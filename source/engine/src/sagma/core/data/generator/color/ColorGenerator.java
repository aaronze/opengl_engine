package sagma.core.data.generator.color;

import sagma.core.data.Color4f;
import sagma.core.data.generator.number.ConstantNumberGenerator;
import sagma.core.data.generator.number.NumberGenerator;
import sagma.core.data.generator.vector.RandomVectorGenerator;
import sagma.core.data.generator.vector.VectorGenerator;
import sagma.core.math.Vec3;

/**
 * </br>Automatically generates a new Color instance every time a Color is requested.
 * </br>This class can be used to generate complex varieties of colors.
 * </br>If no variety of color is needed or wanted consider using ConstantColorGenerator.
 * </br>
 * </br>For example if you wanted a Color field to return any shade of red, but only red
 * </br> you could use the following:
 * </br>
 * </br> NumberGenerator reds = new RandomNumberGenerator(0.0f, 1.0f);
 * </br> NumberGenerator greens = new ConstantNumberGenerator(0.0f);
 * </br> NumberGenerator blues = new ConstantNumberGenerator(0.0f);
 * </br> new ColorGenerator(reds, greens, blues);
 * </br>
 * </br>For another example if you want a grayscale color to slowly increase each time
 * </br>
 * </br> NumberGenerator reds = new LinearNumberGenerator(0.0f, 0.01f, 1.0f);
 * </br> NumberGenerator greens = new LinearNumberGenerator(0.0f, 0.01f, 1.0f);
 * </br> NumberGenerator blues = new LinearNumberGenerator(0.0f, 0.01f, 1.0f);
 * </br> new ColorGenerator(reds, greens, blues);
 * </br>
 * </br>Note in the last example that if the color gets called 100 times, which brings the
 * </br> gray value from 0.0 to 1.0, then the color will loop again starting at 0.
 * 
 * @author Aaron Kison
 *
 */

public class ColorGenerator {
	private NumberGenerator red;
	private NumberGenerator green;
	private NumberGenerator blue;
	private NumberGenerator alpha;
	private VectorGenerator vector;
	private Vec3 vec = new Vec3(0,0,0);
	
	public ColorGenerator(float r, float g, float b) {
		this(r, g, b, 1.0f);
	}
	
	public ColorGenerator(float r, float g, float b, float a) {
		this(new ConstantNumberGenerator(r), new ConstantNumberGenerator(g), new ConstantNumberGenerator(b), new ConstantNumberGenerator(a));
	}
	
	public ColorGenerator(float rMin, float rMax, float gMin, float gMax, float bMin, float bMax) {
		this(new RandomVectorGenerator(rMin, rMax, gMin, gMax, bMin, bMax));
	}
	
	public ColorGenerator(NumberGenerator red, NumberGenerator green, NumberGenerator blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		alpha = new ConstantNumberGenerator(1.0f);
	}
	
	public ColorGenerator(NumberGenerator red, NumberGenerator green, NumberGenerator blue, NumberGenerator alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public ColorGenerator(VectorGenerator vec) {
		vector = vec;
	}
	
	public Color4f nextColor() {
		if (vector != null) {
			vector.setNextVector(this, vec);
			return new Color4f(vec.x, vec.y, vec.z, 1.0f);
		}
		return new Color4f(red.nextNumber(), green.nextNumber(), blue.nextNumber(), alpha.nextNumber());
	}
}
