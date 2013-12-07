package sagma.core.math;

public class FastMath {
	public static float divide(float a, float b) {
		return a/b;
	}
	
	public static int divideBy3(int a) {
		return (int) ((a*2863311531L)>>33);
	}
	
	public static float sin(float a) {
		float a2 = a*a;
		float a3 = a2*a;
		float a5 = a3*a2;
		float a7 = a5*a2;
		float a9 = a7*a2;
		float a11 = a9*a2;
		return  a
				- (divide(a3,6))
				+ (divide(a5,120))
				- (divide(a7,5040))
				+ (divide(a9,362880))
				- (divide(a11,39816800));
		
	}
}
