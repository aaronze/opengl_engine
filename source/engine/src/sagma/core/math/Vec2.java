package sagma.core.math;

import java.awt.Point;

/**
 * An efficient vector representation of a 2D point.
 * @author Aaron Kison
 */
public class Vec2 {
    public float x, y;

    /**
     * </br>Constructs a new 2D vector by doing a deep copy of another vector
     * </br>The new vector exists independently of the other vector
     * @param v Vector to clone
     */
    public Vec2(final Vec2 v) {
        x = v.getX();
        y = v.getY();
    }

    /**
     * </br>Constructs a new 2D vector from the two given components
     * @param x First component
     * @param y Second component
     */
    public Vec2(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * </br>Wraps a 3D vector into a 2D vector by including selected components.
     * </br>Exactly 2 components must be included
     * @param includeX TRUE if x should be included
     * @param includeY TRUE if y should be included
     * @param includeZ TRUE if z should be included
     * @param v
     */
    public Vec2(final Vec3 v, boolean includeX, boolean includeY, boolean includeZ) {
        if (includeX && includeY) { x = v.getX(); y = v.getY(); }
        else if (includeX && includeZ) { x = v.getX(); y = v.getZ(); }
        else if (includeY && includeZ) { x = v.getY(); y = v.getZ(); }
        else throw new RuntimeException("You really do confuse me sometimes.... you know that right?");
    }

    public Vec2(Point location) {
		x = (float)location.getX();
		y = (float)location.getY();
	}

	/**
     * @return Returns the first value of the vector commonly referred to as 'x'
     */
    public final float getX() {
        return x;
    }

    /**
     * @return Returns the second value of the vector commonly referred to as 'y'
     */
    public final float getY() {
        return y;
    }

    /**
     * </br>Adds this vector to another
     * @param v Vector to add
     * @return The resulting vector
     */
    public final Vec2 add(final Vec2 v) {
        return new Vec2(x+v.getX(), y+v.getY());
    }

    /**
     * Adds this vector to the components of another vector
     * @param xVal First component
     * @param yVal Second component
     * @return The resulting vector
     */
    public final Vec2 add(final float xVal, final float yVal) {
        return new Vec2(this.x+xVal, this.y+yVal);
    }

    /**
     * Subtracts another vector from this vector
     * @param v Vector to subtract
     * @return The resulting vector
     */
    public final Vec2 subtract(final Vec2 v) {
        return new Vec2(x-v.getX(), y-v.getY());
    }

    /**
     * Subtracts from this vector the components of another vector
     * @param xVal First component
     * @param yVal Second component
     * @return The resulting vector
     */
    public final Vec2 subtract(final float xVal, final float yVal) {
        return new Vec2(this.x-xVal, this.y-yVal);
    }

    /**
     * @return Length of the vector
     */
    public final float length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * @return Length of the vector before the Math.sqrt operation is performed
     */
    public final float lengthSquared() {
        return x*x+y*y;
    }

    /**
     * Negates this vector by multiplying all components by -1
     * @return Negated vector
     */
    public final Vec2 negate() {
        return new Vec2(-x, -y);
    }

    /**
     * Inverts this vector by divided all components into 1
     * @return Inverted vector
     */
    public final Vec2 invert() {
        return new Vec2(1.0f/x, 1.0f/y);
    }

    /**
     * Calculates the dot product of this vector and another vector
     * @param v Vector to dot
     * @return resulting dot product
     */
    public final float dot(Vec2 v) {
        return x*v.getX() + y*v.getY();
    }

    /**
     * Calculates the distance between this Vector point and another
     * @param v Point to calculate the distance to
     * @return distance between the two points
     */
    public final float distanceTo(Vec2 v) {
        return subtract(v).length();
    }
    
    public void set(float x, float y){
    	this.x = x;
    	this.y = y;
    }

    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    /**
     * This normalizes the vector. :]
     */
    public void m_normalize() {
    	float len = length();
    	
    	x /= len;
    	y /= len;
    }
    
    public Vec2 normalize() {
    	float len = length();
    	
    	return new Vec2(x/len, y/len);
    }
    
    public void m_scale(float val) {
    	x *= val;
    	y *= val;
    }
    
    public Vec2 scale(float val) {
    	return new Vec2(x*val, y*val);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        return (o.getClass() == Vec2.class && o.hashCode() == hashCode());
    }
    
    public Vec2 multiply(Vec2 v) {
    	return new Vec2(v.x*x, v.y*y);
    }

	public void m_add(Vec3 position) {
		x += position.x;
		y += position.y;
	}
}
