package sagma.core.data.model;

import javax.media.opengl.GL2;

import sagma.core.math.Vec3;

public class PolygonBuffer {
	public int numberOfTriangles;
	private VertexBuffer buffer;
	
	public PolygonBuffer(final VertexBuffer buffer) {
		this.buffer = buffer;
		numberOfTriangles = buffer.triangles.capacity();
	}
	
	public static PolygonBuffer buffer(Vec3[] verticies, Vec3[] normals, Vec3[] texCoords, int[] trigs, int[] norms, int[] tex) {
		return buffer(expand(verticies), expand(normals), expand(texCoords), trigs, norms, tex);
	}
	
	public static float[] expand(Vec3[] v) {
		float[] f = new float[v.length*3];
		for (int i = 0; i < v.length; i++) {
			int pos = i*3;
			Vec3 vec = v[i];
			if (vec != null) {
				f[pos] = vec.x;
				f[pos+1] = vec.y;
				f[pos+2] = vec.z;
			}
		}
		return f;
	}
	
	public static PolygonBuffer buffer(float[] verticies, float[] normals, float[] texCoords, 
			int[] triangles, int[] norms, int[] tex) {
		
		return new PolygonBuffer(VertexBuffer.interleave(verticies, normals, texCoords, triangles, norms, tex));
	}
	
	public final void draw(GL2 gl) {	
        buffer.draw(gl);
	}

	
}
