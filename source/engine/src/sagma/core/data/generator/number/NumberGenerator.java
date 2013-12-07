package sagma.core.data.generator.number;

/**
 * <p>
 * A NumberGenerator generates a new number on request, where the
 * new number is created from a defined set of rules.
 * </p>
 * 
 * <p>
 * NumberGenerator can be overriden to return specific values. For example:
 * </p>
 * 
 *<code>
 *</br>NumberGenerator characterPosition = new NumberGenerator() {
 *</br>	  public float nextNumber() {
 *</br>		 return character.getPosition().getX();
 *</br>	  }
 *</br>};
 *</code>
 * 
 * <p>
 * For generating a single-unchanging number use 
 * {@link ConstantNumberGenerator ConstantNumberGenerator}
 * </br>
 * For generating a sequence of numbers use 
 * {@link LinearNumberGenerator LinearNumberGenerator}
 * </br>
 * For generating a random number use 
 * {@link RandomNumberGenerator RandomNumberGenerator}
 * </br>
 * For generating a restricted-interval random number use
 * {@link BundledRandomNumberGenerator BundledRandomNumberGenerator}
 * </p>
 * 
 * @author Aaron Kison
 *
 */
public abstract class NumberGenerator {
	public abstract float nextNumber();
}
