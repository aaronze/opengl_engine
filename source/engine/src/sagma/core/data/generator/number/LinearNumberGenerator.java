package sagma.core.data.generator.number;

/**
 * <p>
 * A <b>LinearNumberGenerator</b> returns a new number that incrementially
 * increases/decreases depending upon the given parameters.
 * </p>
 * 
 * <p>
 * When the LinearNumberGenerator is first made the returned value is the start
 * value.
 * </br>
 * Each time the generator is asked for a number, the number returned increases
 * by the step value.
 * </br>
 * This process continues until the number generated reaches the cap value.
 * </br>
 * When it reaches the cap value one of three things can happen. By default the
 * value will just stay at the cap and not change. The other action is a rollover
 * action where the value will start again from the start value. The third action
 * is the continue action where the value will go past the end value.
 * </br>
 * If these non-default actions are desired, they can be set using 
 * {@link #LinearNumberGenerator(float, float, float, int) 
 * LinearNumberGenerator(start, step, cap, action)}
 *  </p>
 * 
 * @author Aaron Kison
 *
 */
public class LinearNumberGenerator extends NumberGenerator {
	/**
	 * Specifies that the value should return to the start when it reaches the end
	 */
	public final static int ROLLOVER = 1;
	
	/**
	 * Specifies that the value should remain at the end if it reaches the end
	 */
	public final static int CAP = 2;
	
	/**
	 * Specifies that the value should continue past the end
	 */
	public final static int CONTINUE = 3;
	
	private float number = 0;
	private float step;
	private float init = 0;
	private float cap = Float.MAX_VALUE;
	private int action = 2;
	
	/**
	 * Creates a new LinearNumberGenerator with the default CAP option
	 * 
	 * @see LinearNumberGenerator
	 * 
	 * @param start The beginning value for the number
	 * @param step The amount the value should increase each access
	 * @param cap The final value that shall not be exceeded
	 */
	public LinearNumberGenerator(float start, float step, float cap) {
		this.step = step;
		this.cap = cap;
		init = start;
		number = init;
	}
	
	/**
	 * <p>
	 * Creates a new LinearNumberGenerator with the specified cap action
	 * </p>
	 * 
	 * <p>
	 * Possible cap actions are:
	 * <ul>
	 * <li>{@link #ROLLOVER ROLLOVER}
	 * <li>{@link #CAP CAP}
	 * <li>{@link #CONTINUE CONTINUE}
	 * </p>
	 * 
	 * @see LinearNumberGenerator
	 * 
	 * @param start The beginning value for the number
	 * @param step The amount the value should increase each access
	 * @param cap The final value that shall not be exceeded
	 * @param action The action to take when cap is reached
	 */
	public LinearNumberGenerator(float start, float step, float cap, int action) {
		this.step = step;
		this.cap = cap;
		init = start;
		number = init;
		this.action = action;
	}

	@Override
	public float nextNumber() {
		number += step;
		if ((cap > init && number >= cap) || (cap < init && number <= cap)) {
			if (action == ROLLOVER) number = init;
			else if (action == CONTINUE) return number;
			else return cap;
		}
		return number;
	}

}
