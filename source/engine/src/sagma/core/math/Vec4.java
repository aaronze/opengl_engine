package sagma.core.math;

/**
 * Represents a Quaternion
 * @author Aaron Kison
 */
public class Vec4 {
    public float x, y, z, w;

    /**
     * Constructs a new Quaternion (4D vector) using a real (x, y, z) component and a scaling (w) component
     * @param x X Component
     * @param y Y Component
     * @param z Z Component
     * @param w Scaling Component
     */
    public Vec4(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
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
     * @return Returns the third value of the vector commonly referred to as 'z'
     */
    public final float getW() {
       return w;
    }

    /**
     *
     * @return The length of the vector
     */
    public final float length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     *
     * @return The length of the vector before it undergoes a square root operation
     */
    public final float lengthSquared() {
        return x*x + y*y + z*z + w*w;
    }

    /**
     *
     * @return A normalized version of this quaternion
     */
    public final Vec4 normalize() {
        float len = length();
        return new Vec4(x/len, y/len, z/len, w/len);
    }

    /**
     * Multiplies this quaternion with another quaternion
     * @param v Quaternion to multiply by
     * @return Resulting Quaternion
     */
    public final Vec4 multiply(final Vec4 v) {
        return new Vec4(
                w*v.getX() + x*v.getW() + y*v.getZ() - z*v.getY(),
                w*v.getY() - x*v.getZ() + y*v.getW() + z*v.getX(),
                w*v.getZ() + x*v.getY() - y*v.getX() + z*v.getW(),
                w*v.getW() - x*v.getX() - y*v.getY() - z*v.getZ());
    }

    /**
     * Creates a 4x4 matrix which represents the rotation equvilant of this quaternion
     * @return 4x4 rotation matrix
     */
    public final Mat4 generateRotationMatrix() {
        float x2 = x*x;
        float y2 = y*y;
        float z2 = z*z;
        float w2 = w*w;

        float xy2 = x*y*2;
        float wy2 = w*y*2;
        float wz2 = w*z*2;
        float xz2 = x*z*2;
        float yz2 = y*z*2;
        float wx2 = x*w*2;


        return new Mat4(
                w2+x2-y2-z2, xy2-wz2, xz2+wy2, 0,
                xy2+wz2, w2-x2+y2-z2, yz2-wx2, 0,
                xz2-wy2, yz2-wx2, w2-x2-y2+z2, 0,
                0, 0, 0, 1);
    }
    
    public Vec2 xy() {
    	return new Vec2(x, y);
    }
    
    public Vec2 xz() {
    	return new Vec2(x, z);
    }
    
    public Vec2 xw() {
    	return new Vec2(x, w);
    }
    
    public Vec2 yz() {
    	return new Vec2(y, z);
    }
    
    public Vec2 yw() {
    	return new Vec2(y, w);
    }
    
    public Vec2 zw() {
    	return new Vec2(z, w);
    }
    
    public Vec2 yx() {
    	return new Vec2(y, x);
    }
    
    public Vec2 zx() {
    	return new Vec2(z, x);
    }
    
    public Vec2 wx() {
    	return new Vec2(w, x);
    }
    
    public Vec2 zy() {
    	return new Vec2(z, y);
    }
    
    public Vec2 wy() {
    	return new Vec2(w, y);
    }
    
    public Vec2 wz() {
    	return new Vec2(w, z);
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
    
    
}
