package sagma.core.math;

/**
Umm. it's a quad. Four vertecies, you know.
inside the engine its converted to two triangles

Just like the triangle, the quad is also very lenient in how you construct it, with the exception
that each quad must have exactly four non-null verticies.
*/
public class Quad {
	private Vec3[] points;
	private Vec3[] normals;
	private Vec3[] texCoords;

    public Quad(Vec3[] points, Vec3[] norm, Vec3[] texCoords) {
    	this.points = points;
    	this.normals = norm;
    	this.texCoords = texCoords;
    }
    
    public Vec3[] getPoints() {return points;}
    public Vec3[] getTexCoords() {
    	if (texCoords == null) {
    		texCoords = new Vec3[4];
    		texCoords[0] = new Vec3(0, 0, 0);
    		texCoords[1] = new Vec3(1, 0, 0);
    		texCoords[2] = new Vec3(1, 1, 0);
    		texCoords[3] = new Vec3(0, 1, 0);
    	}
    	return texCoords;
    }
    public Vec3[] getNormals() {
        if (normals == null) {
            normals = new Vec3[4];
            Vec3 vLeft = points[1].subtract(points[0]);
            Vec3 vRight = points[2].subtract(points[0]);
            Vec3 normal = vRight.cross(vLeft);
            for (int i = 0; i < 4; i++) {
                normals[i] = normal;
            }
        }
        return normals;
    }

	public void setNormals(Vec3[] normals) {
		this.normals = normals;
	}
	
	public void setTextureCoords(Vec3[] tex) {
		this.texCoords = tex;
	}
    
}