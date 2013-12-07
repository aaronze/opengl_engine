
package sagma.core.math;

/**
 * Represents a 2x2 matrix
 *
 * </br>-------------
 * </br>| m11 | m12 |
 * </br>|-----------|
 * </br>| m21 | m22 |
 * </br>-------------
 *
 * @author Aaron Kison
 */
public class Mat2 {
    public float m11, m12, m21, m22;

    /**
     * Constructs a new 2x2 Matrix from 4 given elements
     * @param a11 Top Left element
     * @param a12 Top Right element
     * @param a21 Bottom Left element
     * @param a22 Bottom Right element
     */
    public Mat2(float a11, float a12, float a21, float a22) {
        m11 = a11;
        m12 = a12;
        m21 = a21;
        m22 = a22;
    }

    /**
     * Constructs a new 2x2 Matrix from the first four elements of a given array
     * @param array
     */
    public Mat2(float[] array) {
        m11 = array[0];
        m12 = array[1];
        m21 = array[2];
        m22 = array[3];
    }

    /**
     * Constructs a new 2x2 Matrix from the first four elements in the 2d array
     * @param array
     */
    public Mat2(float[][] array) {
        m11 = array[0][0];
        m12 = array[0][1];
        m21 = array[1][0];
        m22 = array[1][1];
    }

    /**
     * Scales a matrix by a given constant
     * @param d Constant to scale the matrix
     * @return Scaled Matrix
     */
    public final Mat2 scale(float d) {
        return new Mat2(m11*d, m12*d, m21*d, m22*d);
    }

    /**
     * Multiplies this matrix by another matrix
     * @param m Matrix to multply by
     * @return Resulting multiplied matrix
     */
    public final Mat2 multiply(Mat2 m) {
        return new Mat2(m11*m.m11 + m12*m.m21,
                m11*m.m12 + m12*m.m22,
                m21*m.m11 + m22*m.m21,
                m21*m.m12 + m22*m.m22);
    }

    /**
     * </br>Any matrix multiplied by the identity should be the matrix
     * @return 2x2 identity matrix.
     */
    public static Mat2 identity() {
        return new Mat2(1, 0, 0, 1);
    }

    /**
     * @return A new empty matrix
     */
    public static Mat2 zero() {
        return new Mat2(0, 0, 0, 0);
    }

    @Override
    public String toString() {
        String s = "";
        s += m11 + " " + m12 + "\n";
        s += m21 + " " + m22 + "\n";
        return s;
    }
}
