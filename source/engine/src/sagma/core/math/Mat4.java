package sagma.core.math;

/**
 * Represents a 4x4 matrix
 *
 * </br>-------------------------
 * </br>| m11 | m12 | m13 | m14 |
 * </br>-------------------------
 * </br>| m21 | m22 | m23 | m24 |
 * </br>-------------------------
 * </br>| m31 | m32 | m33 | m34 |
 * </br>-------------------------
 * </br>| m41 | m42 | m43 | m44 |
 * </br>-------------------------
 *
 * @author Aaron Kison
 */
public class Mat4 {
    public float m11, m12, m13, m14;
    public float m21, m22, m23, m24;
    public float m31, m32, m33, m34;
    public float m41, m42, m43, m44;

    /**
     * Constructs a new 4x4 matrix form the given components
     */
    public Mat4(float a11, float a12, float a13, float a14,
            float a21, float a22, float a23, float a24,
            float a31, float a32, float a33, float a34,
            float a41, float a42, float a43, float a44) {
        m11 = a11; m12 = a12; m13 = a13; m14 = a14;
        m21 = a21; m22 = a22; m23 = a23; m24 = a24;
        m31 = a31; m32 = a32; m33 = a33; m34 = a34;
        m41 = a41; m42 = a42; m43 = a43; m44 = a44;
    }

    /**
     * Constructs a new 4x4 matrix from the given array
     * @param a An array of minimum size 16
     */
    public Mat4(float[] a) {
        m11 = a[0]; m12 = a[1]; m13 = a[2]; m14 = a[3];
        m21 = a[4]; m22 = a[5]; m23 = a[6]; m24 = a[7];
        m31 = a[8]; m32 = a[9]; m33 = a[10]; m34 = a[11];
        m41 = a[12]; m42 = a[13]; m43 = a[14]; m44 = a[15];
    }

    /**
     * Constructs a new 4x4 matrix from the given 2d array
     * @param a An array of minimum size 4, 4
     */
    public Mat4(float[][] a) {
        m11 = a[0][0]; m12 = a[0][1]; m13 = a[0][2]; m14 = a[0][3];
        m21 = a[1][0]; m22 = a[1][1]; m23 = a[1][2]; m24 = a[1][3];
        m31 = a[2][0]; m32 = a[2][1]; m33 = a[2][2]; m34 = a[2][3];
        m41 = a[3][0]; m42 = a[3][1]; m43 = a[3][2]; m44 = a[3][3];
    }

    /**
     * Constructs a new 4x4 matrix by wrapping a given Quaternion
     *
     * @param v Quaternion to wrap
     */
    public Mat4(Vec4 v) {
        float a = v.getX(), b = v.getY(), c = v.getZ(), d = v.getW();
        m11 = a; m12 = b; m13 = c; m14 = d;
        m21 = -b; m22 = a; m23 = -d; m24 = c;
        m31 = -c; m32 = d; m33 = a; m34 = -b;
        m41 = -d; m42 = -c; m43 = b; m44 = a;
    }

    /**
     * Scales this matrix by multiplying every component by a given factor
     * @param d Scale factor
     * @return Resulting scaled matrix
     */
    public final Mat4 scale(final float d) {
        return new Mat4(
                m11*d, m12*d, m13*d, m14*d,
                m21*d, m22*d, m23*d, m24*d,
                m31*d, m32*d, m33*d, m34*d,
                m41*d, m42*d, m43*d, m44*d);
    }

    /**
     * Multiplies this matrix by another matrix
     * @param m Matrix to multiply by
     * @return Resulting Matrix
     */
    public final Mat4 multiply(final Mat4 m) {
        return new Mat4(
                m11*m.m11 + m12*m.m21 + m13*m.m31 + m14*m.m41,
                m11*m.m12 + m12*m.m22 + m13*m.m32 + m14*m.m42,
                m11*m.m13 + m12*m.m23 + m13*m.m33 + m14*m.m43,
                m11*m.m14 + m12*m.m24 + m13*m.m34 + m14*m.m44,

                m21*m.m11 + m22*m.m21 + m23*m.m31 + m24*m.m41,
                m21*m.m12 + m22*m.m22 + m23*m.m32 + m24*m.m42,
                m21*m.m13 + m22*m.m23 + m23*m.m33 + m24*m.m43,
                m21*m.m14 + m22*m.m24 + m23*m.m34 + m24*m.m44,

                m31*m.m11 + m32*m.m21 + m33*m.m31 + m34*m.m41,
                m31*m.m12 + m32*m.m22 + m33*m.m32 + m34*m.m42,
                m31*m.m13 + m32*m.m23 + m33*m.m33 + m34*m.m43,
                m31*m.m14 + m32*m.m24 + m33*m.m34 + m34*m.m44,

                m41*m.m11 + m42*m.m21 + m43*m.m31 + m44*m.m41,
                m41*m.m12 + m42*m.m22 + m43*m.m32 + m44*m.m42,
                m41*m.m13 + m42*m.m23 + m43*m.m33 + m44*m.m43,
                m41*m.m14 + m42*m.m24 + m43*m.m34 + m44*m.m44);
    }

    /**
     * Adds another matrix to this one
     * @param m Matrix to add
     * @return Resulting matrix
     */
    public final Mat4 add(Mat4 m) {
        return new Mat4(
                m11+m.m11, m12+m.m12, m13+m.m13, m14+m.m14,
                m21+m.m21, m22+m.m22, m23+m.m23, m24+m.m24,
                m31+m.m31, m32+m.m32, m33+m.m33, m34+m.m34,
                m41+m.m41, m42+m.m42, m43+m.m43, m44+m.m44);
    }

    /**
     * Negates this matrix my multiplying every component by -1
     * @return Resulting negated matrix
     */
    public final Mat4 negate() {
        return scale(-1);
    }

    /**
     * </br>Any matrix multiplied by the identity should be the matrix
     * @return 4x4 identity matrix.
     */
    public static Mat4 identity() {
        return new Mat4(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
    }

    /**
     *
     * @return An empty 4x4 matrix
     */
    public static Mat4 zero() {
        return new Mat4(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public String toString() {
        String s = "";
        s += m11 + " " + m12 + " " + m13 + " " + m14 + "\n";
        s += m21 + " " + m22 + " " + m23 + " " + m24 + "\n";
        s += m31 + " " + m32 + " " + m33 + " " + m34 + "\n";
        s += m41 + " " + m42 + " " + m43 + " " + m44 + "\n";
        return s;
    }
    
    public static Mat4 rotationMatrix(Vec3 rot) {
    	float x = rot.x;
    	float y = rot.y;
    	float z = rot.z;
    	
    	float v = (Math.PI / 180);
    	
    	float sinX = Math.sin(x * v);
    	float sinY = Math.sin(y * v);
    	float sinZ = Math.sin(z * v);
    	float cosX = Math.cos(x * v);
    	float cosY = Math.cos(y * v);
    	float cosZ = Math.cos(z * v);
    	
    	Mat4 mX = new Mat4(1, 0, 0, 0,
    					   0, cosX, sinX, 0,
    					   0, -sinX, cosX, 0,
    					   0, 0, 0, 1);
    	
    	Mat4 mY = new Mat4(cosY, 0, -sinY, 0,
    					   0, 1, 0, 0,
    					   sinY, 0, cosY, 0,
    					   0, 0, 0, 1);
    	
    	Mat4 mZ = new Mat4(cosZ, sinZ, 0, 0, 
    					   -sinZ, cosZ, 0, 0,
    					   0, 0, 1, 0,
    					   0, 0, 0, 1);
    	
    	return mX.multiply(mY).multiply(mZ);
    }
}
