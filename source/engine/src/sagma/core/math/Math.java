package sagma.core.math;

public class Math {
	public final static float PI = 3.14159265359f;
	
	public final static float sin(float degrees) {
		return (float)java.lang.Math.sin(toRadians(degrees));
	}
	
	public final static float cos(float degrees) {
		return (float)java.lang.Math.cos(toRadians(degrees));
	}
	
	public final static float tan(float degrees) {
		return (float)java.lang.Math.tan(toRadians(degrees));
	}
	
	public final static float asin(float val) {
		return toDegrees((float)java.lang.Math.asin(val));
	}
	
	public final static float acos(float val) {
		return toDegrees((float)java.lang.Math.acos(val));
	}
	
	public final static float atan(float val) {
		return toDegrees((float)java.lang.Math.atan(val));
	}
	
	public final static float atan2(float v1, float v2) {
		return toDegrees((float)java.lang.Math.atan2(v1, v2));
	}
	
	public final static float toRadians(float degrees) {
		return (float)(java.lang.Math.toRadians(degrees));
	}
	
	public final static float toDegrees(float rad) {
		return (float)(java.lang.Math.toDegrees(rad));
	}
	
	public final static float sqrt(float val) {
		return (float)java.lang.Math.sqrt(val);
	}
	
	public final static float abs(float val) {
		if (val < 0) return -val;
		return val;
	}
	
	public final static float pow(float val, float raise) {
		return (float)java.lang.Math.pow(val, raise);
	}
	
	public final static float random() {
		return (float)java.lang.Math.random();
	}
	
	public final static Vec2 directionOfAngle(float angle) {
		return new Vec2(sin(angle), cos(angle));
	}
}
