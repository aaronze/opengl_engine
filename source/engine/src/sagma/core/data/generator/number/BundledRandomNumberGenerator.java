package sagma.core.data.generator.number;

/**
 * <p>
 * This generator generates randomly distributed numbers across an interval
 * using a step function.
 * </p>
 * 
 * <p>
 * For example, if an interval of (0 to 1) was given and a step function of 0.1 then
 * the following numbers is a example result:
 * </p>
 * 
 * <p>
 * 0.5, 0.55, 0.45, 0.53, 0.57, 0.65, 0.62, 0.72, 0.82, 0.81
 * </p>
 * 
 * <p>
 * Each number must be no more then the step away from the last number generated.
 * Besides this, each number is uniformly distributed across the restricted interval.
 * This means that for the value of 0.5 and step (0.1) that
 * the next number will fall into the range of (0.4 to 0.6) with equal probability.
 * </p>
 * 
 * <p>
 * Note that poorly given intervals can yield unwanted results. For example giving
 * the interval of (-10 to 10) and a step of (10) would yield something like:
 * </p>
 * 
 * <p>
 * 0.0, 3.6, -4.7, 5.7, 10.0, 10.0, 10.0, 5.9, 10.0, 10.0
 * </p>
 * 
 * <p>
 * This is because all values exceeding the minumum and maximum values are
 * capped. The step size should be at most 1/10th of the interval size.
 * For example for an interval of (-10, to 10) the size is 20 and thus the interval
 * should be at most 2.
 * </p>
 * 
 * <p>
 * When fixing the above source of error, keep in mind that normall you would increase
 * the interval size rather then the interval step, so long that the mid-point
 * stays where you wish. The initial value of this generator is <b>always</b>
 * (min + max) / 2, that is halfway between the minimum and maximum values.
 * </p>
 * 
 * <p>
 * For the above example of the (-10 to 10) interval with a step of 10, the interval
 * should be re-scaled to (-100 to 100) if the step is already the desired size.
 * Please note that values of such magnitude are unlikely to be useful in
 * the engine, and all values should typically be between -1.0 and 1.0
 * </p>
 * 
 * 
 * @author Aaron Kison
 *
 */
public class BundledRandomNumberGenerator extends NumberGenerator {
	private float min;
	private float max;
	private float number;
	
	private NumberGenerator stepper;
	
	/**
	 * @see BundledRandomNumberGenerator
	 */
	public BundledRandomNumberGenerator(float min, float max, float step) {
		this.min = min;
		this.max = max;
		number = (min + max) / 2;
		
		stepper = new RandomNumberGenerator(-step, step);
	}
	
	/**
	 * @see BundledRandomNumberGenerator
	 */
	public BundledRandomNumberGenerator(float min, float max, float minStep, float maxStep) {
		this.min = min;
		this.max = max;
		number = (min + max) / 2;
		
		stepper = new RandomNumberGenerator(minStep, maxStep);
	}
	
	@Override
	public float nextNumber() {
		number += stepper.nextNumber();
		
		if (number > max) number = max;
		if (number < min) number = min;
		
		return number;
	}

}
