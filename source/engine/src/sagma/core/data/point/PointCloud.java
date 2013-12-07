package sagma.core.data.point;

import java.util.ArrayList;
import java.util.Iterator;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.math.ModelDimension;
import sagma.core.math.Triangle;
import sagma.core.math.Vec3;
import sagma.core.profile.Profiler;

public class PointCloud {
	public int POINT_DENSITY = 10;
	int count = 0;
	private ModelDimension dimensions;
	
	private ArrayList<Vec3> points;
	
	public PointCloud() {
		points = new ArrayList<Vec3>();
	}
	
	public void add(Vec3 v) {
		points.add(v);
	}
	
	public void add(Vec3[] v) {
		for (int i = v.length-1; i >= 0; i--) 
			points.add(v[i]);
	}
	
	public void add(ArrayList<Vec3> pnts) {
		Iterator<Vec3> list = pnts.iterator();
		while (list.hasNext()) {
			points.add(list.next());
		}
	}
	
	public void wrap(ArrayList<Triangle> triangles) {
		for (Triangle trig : triangles) {
			add(trig);
		}
	}
	
	public void wrap(Iterator<Triangle> triangles) {
		while (triangles.hasNext())
			add(triangles.next());
	}
	
	/**
	 * Brute-Adds the triangle by scan-cubing.
	 * This is a terrible implementation. Better is calculating the direction
	 * from P1 to P2 then running parallel.
	 * 
	 * @param trig
	 */
	public void add(Triangle trig) {
		Vec3[] pnts = trig.getPoints();
		
		Vec3 A = pnts[0];
		Vec3 B = pnts[1];
		Vec3 C = pnts[2];
		
		// Find bounds
		float minX = min(min(A.x, B.x), C.x);
		float maxX = max(max(A.x, B.x), C.x);
		float minY = min(min(A.y, B.y), C.y);
		float maxY = max(max(A.y, B.y), C.y);
		float minZ = min(min(A.z, B.z), C.z);
		float maxZ = max(max(A.z, B.z), C.z);
		
		float stepX = (maxX-minX) / POINT_DENSITY + 0.0000001f;
		float stepY = (maxY-minY) / POINT_DENSITY + 0.0000001f;
		float stepZ = (maxZ-minZ) / POINT_DENSITY + 0.0000001f;
		
		for (float y = minY; y <= maxY; y += stepY) {
			for (float z = minZ; z <= maxZ; z += stepZ) {
				Vec3 L0 = new Vec3(minX, y, z);
				Vec3 L1 = new Vec3(maxX, y, z);
				if (!trig.intersects(L0, L1)) continue;
				
				for (float x = minX; x <= maxX; x += stepX) {
					Vec3 v = new Vec3(x, y, z);
					if (trig.contains(v)) add(v);
				}
			}
		}
	}
	
	public static float min(float a, float b) {
		if (a < b) 
			return a;
		return b;
	}
	
	public static float max(float a, float b) {
		if (a > b)
			return a;
		return b;
	}
	
	/**
	 * Are you sure you want to do this?
	 * It's terribly inefficent to flat out draw every single point
	 * without discretion.
	 * 
	 * If you want a practical way to render a PointCloud, you
	 * should use StaticModelConstructor.
	 * 
	 * @param drawable
	 */
	public void draw(GLAutoDrawable drawable) {
		Profiler.start("Time Taken to draw zounds of points");
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glBegin(GL.GL_POINTS);
		for (Vec3 v : points) {
			gl.glVertex3f(v.x, v.y, v.z);
		}
		gl.glEnd();
		Profiler.stop("Time Taken to draw zounds of points");
	}

	public ModelDimension getDimensions() {
		if (dimensions == null) {
			float minX = Float.MAX_VALUE;
			float minY = Float.MAX_VALUE;
			float minZ = Float.MAX_VALUE;
			float maxX = -Float.MAX_VALUE;
			float maxY = -Float.MAX_VALUE;
			float maxZ = -Float.MAX_VALUE;
			
			for (Vec3 p : points) {
				float x = p.x;
				float y = p.y;
				float z = p.z;
				
				if (x < minX) minX = x;
				if (y < minY) minY = y;
				if (z < minZ) minZ = z;
				if (x > maxX) maxX = x;
				if (y > maxY) maxY = y;
				if (z > maxZ) maxZ = z;
			}
			
			dimensions = new ModelDimension(minX, minY, minZ, maxX, maxY, maxZ);
		}
			
		return dimensions;
	}

}
