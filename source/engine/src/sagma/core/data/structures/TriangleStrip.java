package sagma.core.data.structures;

import java.util.ArrayList;

import sagma.core.math.Triangle;
import sagma.core.math.Vec3;

public class TriangleStrip {
	private ArrayList<Vec3> vertex;
	private ArrayList<Vec3> normal;
	private ArrayList<Vec3> tex;
	
	public void add(Triangle t) {
		boolean first = vertex == null;
		if (first) {
			vertex = new ArrayList<Vec3>();
			normal = new ArrayList<Vec3>();
			tex = new ArrayList<Vec3>();
		}

		Vec3[] vs = t.getPoints();
		Vec3[] ns = t.getNormals();
		Vec3[] ts = t.getTexCoords();
		
		if (!first) {
			repeatLast();
			vertex.add(vs[0]);
			normal.add(ns[0]);
			tex.add(ts[0]);
		}
		for (int i = 0; i < 3; i++) {
			vertex.add(vs[i]);
			normal.add(ns[i]);
			tex.add(ts[i]);
		}
	}
	
	public ArrayList<Vec3> getVertexArray() {return vertex;}
	public ArrayList<Vec3> getNormalArray() {return normal;}
	public ArrayList<Vec3> getTexCoordArray() {return tex;}
	
	public void add(Vec3 v, Vec3 n, Vec3 t) {
		vertex.add(v);
		normal.add(n);
		tex.add(t);
	}
	
	public float[] vertexBuffer() {
		int size = vertex.size();
		
		float[] v = new float[size*3];
		int counter = 0;
		for (Vec3 vec : vertex) {
			v[counter++] = vec.x;
			v[counter++] = vec.y;
			v[counter++] = vec.z;
		}
		
		return v;
	}
	
	public float[] normalBuffer() {
		int size = normal.size();
		
		float[] v = new float[size*3];
		int counter = 0;
		for (Vec3 vec : normal) {
			v[counter++] = vec.x;
			v[counter++] = vec.y;
			v[counter++] = vec.z;
		}
		
		return v;
	}
	
	public float[] texBuffer() {
		int size = tex.size();
		
		float[] v = new float[size*3];
		int counter = 0;
		for (Vec3 vec : tex) {
			v[counter++] = vec.x;
			v[counter++] = vec.y;
			v[counter++] = vec.z;
		}
		
		return v;
	}
	
	public void repeatLast() {
		vertex.add(vertex.get(vertex.size()-1));
		normal.add(normal.get(normal.size()-1));
		tex.add(tex.get(tex.size()-1));
	}
	
	public static TriangleStrip convert(ArrayList<Triangle> triangles) {
		TriangleStrip strip = new TriangleStrip();
		
		while (triangles.size() > 0) {
			Triangle base = triangles.get(triangles.size()-1);
			strip.add(base);
			triangles.remove(triangles.size()-1);
			int index = 0;
			Vec3[] p = base.getPoints();
			Vec3 a = p[1];
			Vec3 b = p[2];
			
			while ((index = findMatch(triangles, a, b)) != -1) {
				Triangle t = triangles.get(index);
				p = t.getPoints();
				Vec3 p0 = p[0];
				Vec3 p1 = p[1];
				Vec3 p2 = p[2];
				if (p0 != a && p0 != b) {
					strip.add(p0, t.getNormals()[0], t.getTexCoords()[0]);
					a = b;
					b = p0;
				} else if (p1 != a && p1 != b) {
					strip.add(p1, t.getNormals()[1], t.getTexCoords()[1]);
					a = b;
					b = p1;
				} else {
					strip.add(p2, t.getNormals()[2], t.getTexCoords()[2]);
					a = b;
					b = p2;
				}
				triangles.remove(index);
			}
		}
		
		return strip;
	}
	
	private static int findMatch(ArrayList<Triangle> triangles, Vec3 a, Vec3 b) {
		for (int i = 0; i < triangles.size(); i++) {
			Triangle t = triangles.get(i);
			Vec3[] p = t.getPoints();
			
			if (a.equals(p[0]) && (b.equals(p[1]) || b.equals(p[2]))) return i;
			if (a.equals(p[1]) && b.equals(p[2])) return i;
			if (b.equals(p[0]) && (a.equals(p[1]) || a.equals(p[2]))) return i;
			if (b.equals(p[1]) && a.equals(p[2])) return i;
 		}
		
		return -1;
	}
}
