package sagma.core.math;

/**
 * Represents a 3x3 matrix
 *
 * </br>-------------------
 * </br>| m11 | m12 | m13 |
 * </br>|-----------------|
 * </br>| m21 | m22 | m23 |
 * </br>|-----------------|
 * </br>| m31 | m32 | m33 |
 * </br>-------------------
 *
 * @author aaron
 */
public class Mat3 {
    public float m11, m12, m13, m21, m22, m23, m31, m32, m33;

    /**
     * Constructs a new 3x3 Matrix from individual components
     * @param a11 Top left
     * @param a12 Top middle
     * @param a13 Top right
     * @param a21 Middle left
     * @param a22 Middle
     * @param a23 Middle right
     * @param a31 Bottom left
     * @param a32 Bottom middle
     * @param a33 Bottom right
     */
    public Mat3(float a11, float a12, float a13, float a21, float a22, float a23, float a31, float a32, float a33) {
        m11 = a11; m12 = a12; m13 = a13;
        m21 = a21; m22 = a22; m23 = a23;
        m31 = a31; m32 = a32; m33 = a33;
    }

    /**
     * Constructs a 3x3 matrix out of an array of minimum length 9
     * @param a Array to compose into a matrix
     */
    public Mat3(final float[] a) {
        m11 = a[0]; m12 = a[1]; m13 = a[2];
        m21 = a[3]; m22 = a[4]; m23 = a[5];
        m31 = a[6]; m32 = a[7]; m33 = a[8];
    }

    /**
     * Constructs a 3x3 matrix out of a 2D array of size (3, 3);
     * @param a 2D array to compose into a matrix
     */
    public Mat3(final float[][] a) {
        m11 = a[0][0]; m12 = a[0][1]; m13 = a[0][2];
        m21 = a[1][0]; m22 = a[1][1]; m23 = a[1][2];
        m31 = a[2][0]; m32 = a[2][1]; m33 = a[2][2];
    }

    /**
     * Scales this matrix by a given constant
     * @param d Constant to scale by
     * @return Scaled Matrix
     */
    public final Mat3 scale(final float d) {
        return new Mat3(m11*d, m12*d, m13*d, m21*d, m22*d, m23*d, m31*d, m32*d, m33*d);
    }

    /**
     * Mulitplies this matrix by a given matrix
     * @param m Matrix to multiply by
     * @return Resulting matrix
     */
    public final Mat3 multiply(final Mat3 m) {
        return new Mat3(
                m11*m.m11 + m12*m.m21 + m13*m.m31,
                m11*m.m12 + m12*m.m22 + m13*m.m32,
                m11*m.m13 + m12*m.m23 + m13*m.m33,

                m21*m.m11 + m22*m.m21 + m23*m.m31,
                m21*m.m12 + m22*m.m22 + m23*m.m32,
                m21*m.m13 + m22*m.m23 + m23*m.m33,

                m31*m.m11 + m32*m.m21 + m33*m.m31,
                m31*m.m12 + m32*m.m22 + m33*m.m32,
                m31*m.m13 + m32*m.m23 + m33*m.m33);
    }

    /**
     * Adds two matrices together
     * @param m Matrix to add
     * @return Resulting matrix
     */
    public final Mat3 add(final Mat3 m) {
        return new Mat3(
                m11+m.m11, m12+m.m12, m13+m.m13,
                m21+m.m21, m22+m.m22, m23+m.m23,
                m31+m.m31, m32+m.m32, m33+m.m33);
    }

    /**
     * </br>Any matrix multiplied by the identity should be the matrix
     * @return 3x3 identity matrix.
     */
    public static Mat3 identity() {
        return new Mat3(1, 0, 0, 0, 1, 0, 0, 0, 1);
    }

    /**
     * @return An empty 3x3 matrix
     */
    public static Mat3 zero() {
        return new Mat3(0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public String toString() {
        String s = "";
        s += m11 + " " + m12 + " " + m13 + "\n";
        s += m21 + " " + m22 + " " + m23 + "\n";
        s += m31 + " " + m32 + " " + m33 + "\n";
        return s;
    }

}
