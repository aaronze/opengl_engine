package sagma.core.math;

import sagma.core.material.Material;

/**
 * Represents a physical 3D triangle
 * 
 * A Triangle MUST be constructed with exactly 3 defined Vec3's. 
 * If any of the Vectors are null then the triangle will crash the application when accessed.
 * Detecting null vectors is not the responsibility of this triangle.
 * 
 * However the triangle is much more lenient when given null values for normals and texture co-ordinates.
 * If the normal vector array is given as null, then the corresponding normals will be created
 *  when access is attempted. The normals will not be perfect, but a good enough representation.
 *  
 * The textue co-ordinates will be generated as well if they are missing, it will be a horrible representation
 *  but sometimes you don't care. For example when it's not textured.
 * 
 * @author Aaron Kison
 *
 */
public class Triangle {
	private Vec3[] points;
    private Vec3[] normals;
    private Vec3[] texCoords;
    private Material material;
    private static Vec3 n = new Vec3(0,0,0);
    private static Vec3 u = new Vec3(0,0,0);
    private static Vec3 v = new Vec3(0,0,0);
    private static Vec3 dir = new Vec3(0,0,0);
    private static Vec3 w0 = new Vec3(0,0,0);

    /**
     * Constructs a new triangle with the given vertex points, normals and texture co-ordinates
     * Triangles will happily build with both normals and texture co-ordinates as null.
     * 
     * @param points Vertex points
     * @param normals Normal vectors for each vertex
     * @param texCoords Texture Co-ordinates for each vertex
     */
    public Triangle(Vec3[] points, Vec3[] normals, Vec3[] texCoords) {
    	this.points = points;
    	this.normals = normals;
    	this.texCoords = texCoords;
    }
    
    /**
     * TODO: Remove Vec3 creations by using inplace
     */
    public static Vec3 calculateNormal(Vec3 a, Vec3 b, Vec3 c) {
    	return a.subtract(b).cross(a.subtract(c)).normalize();
    }

    public Vec3[] getNormals() {
        if (normals == null) {
            normals = new Vec3[3];
            u.set(points[1]);
            u.m_subtract(points[0]);
            v.set(points[2]);
            v.m_subtract(points[0]);
            u.m_cross(v);
            Vec3 normal = new Vec3(u);
            for (int i = 0; i < 3; i++) {
                normals[i] = normal.negate();
            }
        }
        return normals;
    }
    
    public Vec3[] getTexCoords() {
    	if (texCoords == null) {
    		texCoords = new Vec3[3];
    		texCoords[0] = new Vec3(0, 0, 0);
    		texCoords[1] = new Vec3(1, 0, 0);
    		texCoords[2] = new Vec3(1, 1, 0);
    	}
    	return texCoords;
    }
    
    public void setPoints(Vec3[] v) {
    	points = v;
    }

    public Vec3[] getPoints() {
        return points;
    }

    public void setMaterial(Material m) {
        material = m;
    }

    public Material getMaterial() {
    	return material;
    }
    
    public boolean intersects(Triangle t) {
    	Vec3 a = points[0];
    	Vec3 b = points[1];
    	Vec3 c = points[2];
    	
    	boolean ab = t.intersects(a, b);
    	boolean bc = t.intersects(b, c);
    	boolean ac = t.intersects(a, c);
    	
    	return ab || bc || ac;
    }
    
    public boolean intersects(Vec3 R0, Vec3 R1) {
    	u.set(points[1]);
    	u.m_subtract(points[0]);
    	
    	v.set(points[1]);
    	v.m_subtract(points[0]);
    	
    	n.set(u);
    	n.m_cross(v);
    	
    	if (n.equals(Vec3.EMPTY)) return false;
    	
    	dir.set(R1);
    	dir.m_subtract(R0);
    	w0.set(R0);
    	w0.m_subtract(points[0]);
    	
    	float a = -n.dot(w0);
    	float b = n.dot(dir);
    	
    	if (Math.abs(b) < 0.0000001f) {
    		if (a == 0) return true;
    		return false;
    	}
    	
    	float r = a / b;
    	if (r < 0) return false;
    	
    	dir.m_scale(r);
    	R0.m_add(dir);
    	R0.m_subtract(points[0]);
    	
    	float uu = u.dot(u);
    	float uv = u.dot(v);
    	float vv = v.dot(v);
    	
    	float wu = R0.dot(u);
    	float wv = R0.dot(v);
    	
    	float D = uv*uv - uu*vv;
    	
    	float s = (uv * wv - vv * wu) / D;
    	if (s < 0 || s > 1) return false;
    	
    	float t = (uv * wu - uu * wv) / D;
    	if (t < 0 || (s + t) > 1) return false;

    	return true;
    }
    
    @Override
	public String toString() {
    	String s = "";
    	
    	s = "{" + points[0] + ", " + points[1] + ", " + points[2] + "}";
    	
    	return s;
    }

	public boolean contains(Vec3 vec) {
		Vec3 A = points[0];
		Vec3 B = points[1];
		Vec3 C = points[2];
		
		return sameSide(vec, A, B, C) && sameSide(vec, B, A, C) && sameSide(vec, C, A, B);
	}
	
	private static boolean sameSide(Vec3 p1, Vec3 p2, Vec3 a, Vec3 b) {
		float v0x = b.x - a.x;
		float v0y = b.y - a.y;
		float v0z = b.z - a.z;
		
		float v1x = p1.x - a.x;
		float v1y = p1.y - a.y;
		float v1z = p1.z - a.z;
		
		float vc1x = v0y*v1z - v0z*v1y;
		float vc1y = v0z*v1x - v0x*v1z;
		float vc1z = v0x*v1y - v0y*v1x;
		
		float v3x = p2.x - a.x;
		float v3y = p2.y - a.y;
		float v3z = p2.z - a.z;
		
		float vc2x = v0y*v3z - v0z*v3y;
		float vc2y = v0z*v3x - v0x*v3z;
		float vc2z = v0x*v3y - v0y*v3x;
		
		float dot = vc1x*vc2x + vc1y*vc2y + vc1z*vc2z;
	
		return dot >= 0;
	}
	
}