package sagma.core.math;

/**
 * Describes a Vector of size 3 that is a 3 Dimensional vector
 * 
 * <p>
 * Note: all standard methods are Immutable, i.e. they do not modify the original
 * contents of the vector. (vector.add(vector)) for example will not modify either vector
 * but produce a new vector containing the result.
 * </p>
 * 
 * <p>
 * If mutable versions are required (for performance or memory contraints) the
 * more efficient m_methodName is given. For example: vector1.m_add(vector2) will
 * modify vector1 to the result. Vector 1's original contents are overwritten.
 * </p>
 * 
 * @author Aaron Kison
 */
public class Vec3 {
	public final static Vec3 EMPTY = new Vec3(0, 0, 0);
    public float x, y, z;

    /*
     * </br>Constructs a new 3D Vector from a previous Vector by doing a deep copy.
     * </br>That is this Vector will have a different Object ID and its contents will
     * be stored seperately in memory.
     */
    public Vec3(final Vec3 v) {
    	this(v.x, v.y, v.z);
    }

    /**
     * Wraps a 2D vector into a 3D one. The third 'z' component is set to 0.
     */
    public Vec3(final Vec2 v) {
    	this(v.x, v.y, 0);
    }

    /**
     * Constructs a new 3D Vector by wrapping a 2D vector and adding a third component.
     *
     * @param v 2D vector to wrap that includes x, y
     * @param z the third value to be wrapped
     */
    public Vec3(final Vec2 v, final float z) {
    	this(v.x, v.y, z);
    }

    /**
     * Constructs a new 3D Vector by first adding a component and then wrapping a 2D vector
     *
     * @param v 2D vector to wrap that includes y, z
     * @param x the first value to be wrapped
     */
    public Vec3(final float x, final Vec2 v) {
        this(x, v.x, v.y);
    }

    /**
     * Creates a new 3D vector by wrapping three values
     * @param x First component of vector
     * @param y Second component of vector
     * @param z Third component of vector
     */
    public Vec3(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void set(float x, float y, float z) {
    	this.x = x;
    	this.y = y;
    	this.z = z;
    }
    
    public void set(Vec3 v) {
    	x = v.x;
    	y = v.y;
    	z = v.z;
    }

    /**
     * </br>Wraps a Quaternion (4D Vector) into a 3D one.
     * </br>This happens by scaling the fourth component of the Vec4 by the three other components
     * </br>This forces a simplification of the quaternion
     * @param v Quaternion to wrap
     */
    public Vec3(final Vec4 v) {
    	this(v.x*v.w, v.y*v.w, v.z*v.w);
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
     * @return Returns the third value of the vector commonly referred to as 'z'
     */
    public final float getZ() {
        return z;
    }

    /**
     * </br>Adds this vector and another vector together
     * </br>This method does NOT affect the contents of this vector
     * @param v Vector to add
     * @return Resulting vector of the addition
     */
    public final Vec3 add(final Vec3 v) {
    	return add(v.x, v.y, v.z);
    }
    
    /**
     * </br>Adds this vector and another vector (x, y) components together
     * </br>This method does NOT affect the contents of this vector
     * @param v Vector to add
     * @return Resulting vector of the addition
     */
    public final Vec3 add(final Vec2 v) {
    	return add(v.x, v.y, 0);
    }

    /**
     * </br>Adds this vector and another vector (y, z) with another value (x) together
     * </br>This method does NOT affect the contents of this vector
     * @param v Vector to add
     * @return Resulting vector of the addition
     */
    public final Vec3 add(final float xVal, final Vec2 v) {
        return add(xVal, v.x, v.y);
    }

    /**
     * </br>Adds this vector and another vector (x, y) with another value (z) together
     * </br>This method does NOT affect the contents of this vector
     * @param v Vector to add
     * @return Resulting vector of the addition
     */
    public final Vec3 add(final Vec2 v, final float zVal) {
    	return add(v.x, v.y, zVal);
    }

    /**
     * </br>Adds this vector and a quaternion by first converting the quaternion into a 3D Vector
     * </br>This method does NOT affect the contents of this vector
     * @param v Vector to add
     * @return Resulting vector of the addition
     */
    public final Vec3 add(final Vec4 v) {
    	return add(v.x*v.w, v.y*v.w, v.z*v.w);
    }

    /**
     * Adds this vector to three individual components of a vector
     * @param xVal First component of the vector
     * @param yVal Second component of the vector
     * @param zVal Third component of the vector
     * @return Vector of the addition between these two vectors
     */
    public final Vec3 add(final float xVal, final float yVal, final float zVal) {
        return new Vec3(this.x+xVal, this.y+yVal, this.z+zVal);
    }

    /**
     * @return Returns the length of this vector
     */
    public final float length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * @return Returns the length of this vector before its undergone its Math.sqrt() function thus saving CPU
     */
    public final float lengthSquared() {
        return x*x+y*y+z*z;
    }

    /**
     * </br>Normalizes this vector by divided each value by the vector's length.
     * </br>This method does NOT alter this vector's value
     * @return Normalized vector
     */
    public final Vec3 normalize() {
        return scale(1.0f/length());
    }

    /**
     * @return Returns a vector that is the opposite of this vector
     */
    public final Vec3 negate() {
        return scale(-1);
    }

    /**
     * </br>Subtracts this vector by another vector
     * </br>This method does NOT alter this vector's value
     * @param v Vector to subtract
     * @return A vector resulting from the subtract of this vector by another
     */
    public final Vec3 subtract(final Vec3 v) {
    	return subtract(v.x, v.y, v.z);
    }

    /**
     * </br>Subtracts this vector by a Quaternion which we be automatically converted into a Vector
     * </br>This method does NOT alter this vector's value
     * @param v
     * @return
     */
    public final Vec3 subtract(final Vec4 v) {
    	return subtract(v.x*v.w, v.y*v.w, v.z*v.w);
    }

    /**
     * </br>Subtracts this vector by a (x, y) vector and a third value z
     * @param v (x, y) Vector
     * @param zVal Third component
     * @return Vector of the subtraction
     */
    public final Vec3 subtract(final Vec2 v, final float zVal) {
    	return subtract(v.x, v.y, zVal);
    }

    /**
     * </br>Subtracts this vector by a (x, y) vector and a third value z
     * @param v (x, y) Vector
     * @param z Third component
     * @return Vector of the subtraction
     */
    public final Vec3 subtract(final float xVal, final Vec2 v) {
    	return subtract(xVal, v.x, v.y);
    }

    /**
     * </br>Subtacts this vector by three components of a vector
     * @param xVal First component
     * @param yVal Second component
     * @param zVal Third component
     * @return Vector of the subtraction
     */
    public final Vec3 subtract(final float xVal, final float yVal, final float zVal) {
        return new Vec3(this.x-xVal, this.y-yVal, this.z-zVal);
    }

    /**
     * </br>Scales a vector by a set amount, that is each component of this vector
     * is multipied by a given factor
     * </br>A more efficient approach is to use a Vec4, where the 'w' or fourth component
     * of the vector represents the scale.
     * </br>This method does NOT alter this vector
     * @param d scaling factor
     * @return Vector scaled by given factor
     */
    public final Vec3 scale(float d) {
        return new Vec3(x*d, y*d, z*d);
    }

    /**
     * </br>Multiply isn't an accurate enough description of what this function should do,
     * cross() or dot() should be used instead.
     * </br>Included for identification of multiplication terms. Most multiplication
     * is cross product anyway, which is what this method does.
     * @param v Vector to multiply
     * @return Vector resulting of the cross product
     * @deprecated
     */
    public final Vec3 multiply(final Vec3 v) {
        return cross(v);
    }

    /**
     * </br>Multiplies this vector by another using cross multiplication
     * </br>This method does NOT alter this vector
     * @param v Vector to multiply by
     * @return Multiplied vector
     */
    public final Vec3 cross(final Vec3 v) {
        return new Vec3(y*v.getZ() - z*v.getY(), z*v.getX() - x*v.getZ(), x*v.getY() - y*v.getX());
    }

    /**
     * </br>Calculates the dot product of the two vectors
     * @param v Vector to dot by
     * @return Value of the dot product
     */
    public final float dot(final Vec3 v) {
        return x*v.getX() + y*v.getY() + z*v.getZ();
    }


    /**
     * </br>Divides this vector by another vector.
     * </br>This method is extremely bad form, there must be a better way to do
     * whatever you are trying to do. Think really hard and mutter incoherently
     * to yourself, and then when you are sure you have no better option continue
     * using this.
     * @param v Vector to divide by
     * @return Divided vector
     * @deprecated
     */
    public final Vec3 divide(final Vec3 v) {
        return multiply(v.invert());
    }

    /**
     * </br>Inverts this vector by divided all the values into 1.0
     * @return Inverted vector
     */
    public final Vec3 invert() {
        return new Vec3(1.0f/x, 1.0f/y, 1.0f/z);
    }

    /**
     * </br>Another method to try to avoid.
     * </br>It will happily do what you want but usually there is a better
     * way of doing it.
     * </br>Un-scales a value by a given amount by multiplying all components by the
     * inversion of the given value
     * @param d Value to divide by
     * @return scaled Vector
     */
    public final Vec3 divide(final float d) {
        final float notD = 1.0f/d;
        return new Vec3(x*notD, y*notD, z*notD);
    }

    /**
     * Calculates the distance between this Vector point and another
     * @param v Point to calculate the distance to
     * @return distance between the two points
     */
    public final float distanceTo(Vec3 v) {
        return subtract(v).length();
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass().equals(Vec3.class)) {
        	Vec3 vec = (Vec3)o;
        	
        	return (vec.x == x && vec.y == y && vec.z == z);
        }
        return false;
    }
    
    public Vec3 rotateAround(Vec3 centre, float angle) {
		Vec3 dir = subtract(centre);
		float xVal = dir.x;
		float yVal = dir.y;
		float xi = (xVal * Math.cos(angle)) - (yVal * Math.sin(angle));
		float yi = (xVal * Math.sin(angle)) + (yVal * Math.cos(angle));
		return centre.add(xi, yi, 0);
	}
    
    public Vec2 xy() {
    	return new Vec2(x, y);
    }
    
    public Vec2 xz() {
    	return new Vec2(x, z);
    }
    
    public Vec2 yz() {
    	return new Vec2(y, z);
    }
    
    public Vec2 yx() {
    	return new Vec2(y, x);
    }
    
    public Vec2 zx() {
    	return new Vec2(z, x);
    }
    
    public Vec2 zy() {
    	return new Vec2(z, y);
    }
    
    public Vec3 xyz() {
    	return new Vec3(x, y, z);
    }
    
    public Vec3 xzy() {
    	return new Vec3(x, z, y);
    }
    
    public Vec3 yxz() {
    	return new Vec3(y, x, z);
    }
    
    public Vec3 yzx() {
    	return new Vec3(y, z, x);
    }
    
    public Vec3 zxy() {
    	return new Vec3(z, x, y);
    }
    
    public Vec3 zyx() {
    	return new Vec3(z, y, x);
    }
    
    /**
     * Adds the given vector to this vector, modifying the original.
     * 
     * Use only in areas of high traffic.
     */
    public void m_add(Vec3 v) {
    	x += v.x;
    	y += v.y;
    	z += v.z;
    }
    
    /**
     * Adds the given vector to this vector, modifying the original.
     * 
     * Use only in areas of high traffic.
     */
    public void m_add(Vec4 v) {
    	x += v.x;
    	y += v.y;
    	z += v.z;
    }
    
    /**
     * Adds the given vector to this vector, modifying the original.
     * 
     * Use only in areas of high traffic.
     */
    public void m_add(Vec2 v) {
    	x += v.x;
    	y += v.y;
    }
    
    /**
     * Adds the given vector to this vector, modifying the original.
     * 
     * Use only in areas of high traffic.
     */
    public void m_add(Vec2 v, float zVal) {
    	x += v.x;
    	y += v.y;
    	this.z += zVal;
    }
    
    /**
     * Adds the given vector to this vector, modifying the original.
     * 
     * Use only in areas of high traffic.
     */
    public void m_add(float xVal, Vec2 v) {
    	this.x += xVal;
    	y += v.x;
    	z += v.y;
    }
    
    /**
     * Adds the given vector to this vector, modifying the original.
     * 
     * Use only in areas of high traffic.
     */
    public void m_add(float i, float j, float k) {
    	x += i;
    	y += j;
    	z += k;
    }
    
    /**
     * Subtracts the given vector to this vector, modifying the original.
     * 
     * Use only in areas of high traffic.
     */
    public void m_subtract(Vec3 v) {
    	x -= v.x;
    	y -= v.y;
    	z -= v.z;
    }
    
    /**
     * Cross multiplies this vector by a given vector, modifying the original
     * 
     * Use only in areas of high traffic.
     */
    public void m_cross(Vec3 v) {
    	x = y*v.getZ() - z*v.getY();
    	y = z*v.getX() - x*v.getZ(); 
    	z = x*v.getY() - y*v.getX();
    }
    
    /**
     * Scales this vector by a given value, modifying the original
     * 
     * Use only in areas of high traffic
     * 
     */
    public void m_scale(float f) {
    	x *= f;
    	y *= f;
    	z *= f;
    }
    
    /**
     * Normalizes this vector, modifying the original
     * 
     * Use only in areas of high traffic
     */
    public void m_normalize() {
    	float len = length();
    	x /= len;
    	y /= len;
    	z /= len;
    }

    /**
     * Negates this vector, modifying the original
     * 
     * Use only in areas of high traffic
     */
    public void m_negate() {
    	x = -x;
    	y = -y;
    	z = -z;
    }
    
    public final void m_set(final float xVal, final float yVal, final float zVal) {
    	this.x = xVal;
    	this.y = yVal;
    	this.z = zVal;
    }
    
    public final void m_set(final Vec3 v) {
    	this.x = v.x;
    	this.y = v.y;
    	this.z = v.z;
    }
}
