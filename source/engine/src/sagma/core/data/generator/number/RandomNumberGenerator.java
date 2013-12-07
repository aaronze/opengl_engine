package sagma.core.data.generator.number;

/**
 * <p>
 * Generates a random number using the specified options.
 * </p>
 * 
 * <p>
 * </br>min - The smallest number that should be generated. Default is 0.
 * </br>max - The largest number that should be generated. Default is 1.
 * </br>seed - A non-zero integer that is used as the basis of all random numbers.
 * </p>
 * 
 * @author Aaron Kison
 */
public class RandomNumberGenerator extends NumberGenerator {
    private int w, z;
    private float min, max;

    /**
     * Generates a new random number generator with a random seed,
     * a min value of 0 and a max value = 1.
     */
    public RandomNumberGenerator() {
    	w = (int)(Math.random()*1000);
    	min = 0.0f;
    	max = 1.0f;
    }
    
    /**
     * Generates a new random number generator with a random seed and 
     * a min value of 0.
     * 
     * @param max Maximum value generator can generate
     */
    public RandomNumberGenerator(float max) {
    	w = (int)(Math.random()*1000);
    	min = 0.0f;
    	this.max = max;
    }
    
    /**
     * Generates a new random number generator with a min value of 0
     * 
     * @param max Maximum value generator can generate
     * @param seed A non-zero number
     */
    public RandomNumberGenerator(float max, int seed) {
    	w = seed;
    	min = 0.0f;
    	this.max = max;
    }
    
    /**
     * Generates a new random number generator with a random seed
     * 
     * @param min Minumum value generator can generate
     * @param max Maximum value generator can generate
     */
    public RandomNumberGenerator(float min, float max) {
    	w = (int)(Math.random()*1000);
    	this.min = min;
    	this.max = max;
    }
    
    /**
     * Generates a new random number generator
     * 
     * @param min Minumum value generator can generate
     * @param max Maximum value generator can generate
     * @param seed A non-zero number
     */
    public RandomNumberGenerator(float min, float max, int seed) {
    	w = seed;
    	this.min = min;
    	this.max = max;
    }
    
    private long getRandom() {
    	z = 36969 * (z & 65535) + (z >> 16);
        w = 18000 * (w & 65535) + (w >> 16);
        return (z << 16) + (w & 65535);
    }
    
    @Override
	public float nextNumber() {
    	float num = getRandom();
    	num %= 10000;
    	num /= 10000;
    	num *= (max-min);
    	num += min;
    	return num;
    }
    
    public void setMin(float min){
    	this.min = min;
    }
    public void setMax(float max){
    	this.max = max;
    }
    public float getMin(){
    	return min;
    }
    public float getMax(){
    	return max;
    }
    
}
