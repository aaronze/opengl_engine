package sagma.core.math;

/**
It's a 3D point for making shapes.
 */
public class Vertex {

    private float x, y, z;

    /**
    Constructs a vertex out of the given points.
    
    @param x List of x values
    @param y List of y values
    @param z List of z values
    @param size How many points to take from each array of values. Must be less then or equal to the sizes of the arrays.
     */
    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void dispose() {
        x = 0;
        y = 0;
        z = 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Vertex cross(Vertex v) {
        return new Vertex(y * v.getZ() - z * v.getY(), z * v.getX() - x * v.getZ(), x * v.getY() - y * v.getX());
    }

    public Vertex minus(Vertex v) {
        Vertex answer = new Vertex(x - v.getX(), y - v.getY(), z - v.getZ());
        return answer;
    }
}