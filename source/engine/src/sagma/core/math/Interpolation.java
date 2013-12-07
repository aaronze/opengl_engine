package sagma.core.math;

/**
 * Iterpolates between values
 * 
 * <p>
 * For example if the first value is 0, and the second is 10 and you wanted
 * to get the midpoint then using 0.5f as X, then linear interpolation returns 5.
 * </p>
 * 
 * @author Aaron Kison
 *
 */
public class Interpolation {
	public static float linearInterpolate(float a, float b, float x) {
		return a*(1-x) + b*x;
	}
	
	public static float cosineInterpolate(float a, float b, float x) {
		float ft = Math.PI*x;
		float f = (1-Math.cos(ft)) * 0.5f;
		return a*(1-f) + b*f;
	}
	
	/*
	 * Interpolates using the neighbours to balance the points. 
	 * 
	 * <p>
	 * If the value between index points A and B then:
	 * <ul>
	 * <li>aPrev = A-1
	 * <li>a = A
	 * <li>b = B
	 * <li>bNext = B+1
	 * </ul>
	 * </p>
	 */
	public static float cubicInterpolate(float aPrev, float a, float b, float bNext, float x) {
		float p = (bNext-b) - (aPrev-a);
		float q = (aPrev-a) - p;
		float r = b-aPrev;
		
		float x2 = x*x;
		float x3 = x2*x;
		
		return p*x3 + q*x2 + r*x + a;
	}
}
