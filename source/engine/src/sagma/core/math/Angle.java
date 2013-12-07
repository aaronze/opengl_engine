package sagma.core.math;

import static sagma.core.math.Math.*;

/**
 * Represents a true angle in degrees that wraps appropriately 
 * and compares appropriately, that is that the difference between the 
 * angles 360 and 0 is 0.
 * 
 * Guaranteed to always hold a value between 0 and 359.999~9
 * @author Aaron Kison
 *
 */
public class Angle implements Comparable<Angle> {
	private float angle;
	
	public Angle(float val) {
		angle = wrap(val);
	}
	
	private static float wrap(float angle) {
		while (angle >= 360.0f) angle -= 360.0f;
		while (angle < 0.0f) angle += 360.0f;
		return angle;
	}
	
	public float angleValue() {
		return angle;
	}
	
	public void add(float v) {
		this.angle += v;
		this.angle = wrap(this.angle);
	}
	
	public void add(Angle val) {
		add(val.angleValue());
	}
	
	public void subtract(float val) {
		this.angle -= val;
		this.angle = wrap(this.angle);
	}
	
	public void subtract(Angle val) {
		subtract(val.angleValue());
	}
	
	public boolean equals(float f) {
		return (compareTo(f) == 0);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass() == Angle.class) {
			
		}
		return false;
	}
	
	public int compareTo(float val) {
		val = wrap(val);
		
		if (abs(angle-val) > 180) {
			val = -360 + val;
		}
		
		if (val == angle) return 0;
		if (val > angle) return 1;
		return -1;
	}

	@Override
	public int compareTo(Angle o) {
		return compareTo(o.angleValue());
	}
	
	public void set(float angle) {
		this.angle = wrap(angle);
	}
	
	
}
