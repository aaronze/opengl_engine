package sagma.core.data;

import java.util.ArrayList;
import java.util.Iterator;

import sagma.core.event.CollisionEvent;
import sagma.core.math.Triangle;
import sagma.core.math.Vec3;
import sagma.core.model.Model;

public class Octree {
	public int MAX_DEPTH = 3;
	
	private Octree XYZ;
	private Octree WYZ;
	private Octree XHZ;
	private Octree WHZ;
	private Octree XYD;
	private Octree WYD;
	private Octree XHD;
	private Octree WHD;
	
	private int depth;
	private Vec3 centre;
	private Vec3 size;
	
	private float EPSILON = 0.0000001f;
	private static Vec3 v = new Vec3(0,0,0);
	
	//private ArrayList<Triangle> data = new ArrayList<Triangle>();
	private boolean hasData = false;
	
	public Octree(Vec3 mid, Vec3 size) {
		this(mid, size, 0, 3);
	}
	
	public Octree(int depth, Vec3 mid, Vec3 size) {
		this(mid, size, 0, depth);
		
	}
	
	private boolean isEqual(float a, float b) {
		return Math.abs(a-b) < EPSILON;
	}
	
	private boolean isGreater(float a, float b) {
		return (a-b)-EPSILON > 0.0f;
	}
	
	private Octree(Vec3 mid, Vec3 size, int depth, int maxDepth) {
		MAX_DEPTH = maxDepth;
		this.depth = depth;
		centre = mid;
		this.size = size;
		
		if (depth < MAX_DEPTH) {
			float halfX = size.x * 0.5f;
			float halfY = size.y * 0.5f;
			float halfZ = size.z * 0.5f;
			
			float L = centre.x - halfX;
			float R = centre.x + halfX;
			float U = centre.y + halfY;
			float D = centre.y - halfY;
			float F = centre.z - halfZ;
			float B = centre.z + halfZ;
			
			Vec3 halfSize = size.scale(0.5f);
			
			XYZ = new Octree(new Vec3(L, D, F), halfSize, depth+1, maxDepth);
			WYZ = new Octree(new Vec3(R, D, F), halfSize, depth+1, maxDepth);
			XHZ = new Octree(new Vec3(L, U, F), halfSize, depth+1, maxDepth);
			WHZ = new Octree(new Vec3(R, U, F), halfSize, depth+1, maxDepth);
			
			XYD = new Octree(new Vec3(L, D, B), halfSize, depth+1, maxDepth);
			WYD = new Octree(new Vec3(R, D, B), halfSize, depth+1, maxDepth);
			XHD = new Octree(new Vec3(L, U, B), halfSize, depth+1, maxDepth);
			WHD = new Octree(new Vec3(R, U, B), halfSize, depth+1, maxDepth);
		}
	}
	
	public final void add(Triangle t) {
		if (depth >= MAX_DEPTH) {
			hasData = true;
			return;
		}
			
		Vec3[] searchPoints = t.getPoints();
		Vec3 a = searchPoints[0];
		Vec3 b = searchPoints[1];
		Vec3 c = searchPoints[2];
		
		if (!(isValid(a) && isValid(b) && isValid(c))) {
			return;
		}
		
		Vec3 mid = centre;
		
		// How many corners intersect the axis?
		int xCount = 0;
		int yCount = 0;
		int zCount = 0;

		if (isEqual(a.x, mid.x)) xCount++;
		if (isEqual(b.x, mid.x)) xCount++;
		if (isEqual(c.x, mid.x)) xCount++;
		
		if (isEqual(a.y, mid.y)) yCount++;
		if (isEqual(b.y, mid.y)) yCount++;
		if (isEqual(c.y, mid.y)) yCount++;
		
		if (isEqual(a.z, mid.z)) zCount++;
		if (isEqual(b.z, mid.z)) zCount++;
		if (isEqual(c.z, mid.z)) zCount++;
		
		boolean xSignA = isGreater(a.x, mid.x);
		boolean xSignB = isGreater(b.x, mid.x);
		boolean xSignC = isGreater(c.x, mid.x);
		
		boolean ySignA = isGreater(a.y, mid.y);
		boolean ySignB = isGreater(b.y, mid.y);
		boolean ySignC = isGreater(c.y, mid.y);
		
		boolean zSignA = isGreater(a.z, mid.z);
		boolean zSignB = isGreater(b.z, mid.z);
		boolean zSignC = isGreater(c.z, mid.z);
		
		if (xCount == 0) {
			if ((xSignA != xSignB) || (xSignB != xSignC)) {
				splitTriangle2X(t);
				return;
			}
		} else if (xCount == 1) {
			if ((isEqual(a.x, mid.x) && (xSignB != xSignC))
					|| (isEqual(b.x, mid.x) && (xSignA != xSignC))
					|| (isEqual(c.x, mid.x) && (xSignA != xSignB))) {
				splitTriangle1X(t);
				return;
			}
		}
		
		if (yCount == 0) {
			if ((ySignA != ySignB) || (ySignB != ySignC)) {
				splitTriangle2Y(t);
				return;
			}
		} else if (yCount == 1) {
			if ((isEqual(a.y, mid.y) && (ySignB != ySignC))
					|| (isEqual(b.y, mid.y) && (ySignA != ySignC))
					|| (isEqual(c.y, mid.y) && (ySignA != ySignB))) {
				splitTriangle1Y(t);
				return;
			}
		}
		
		if (zCount == 0) {
			if ((zSignA != zSignB) || (zSignB != zSignC)) {
				splitTriangle2Z(t);
				return;
			}
		} else if (zCount == 1) {
			if ((isEqual(a.z, mid.z) && (zSignB != zSignC))
					|| (isEqual(b.z, mid.z) && (zSignA != zSignC))
					|| (isEqual(c.z, mid.z) && (zSignA != zSignB))) {
				splitTriangle1Z(t);
				return;
			}
		}
		
		// Triangle is okay to be added.
		boolean x = xSignC;
		boolean y = ySignC;
		boolean z = zSignC;
		
		if (!isEqual(a.x,mid.x)) x = xSignA;
		else if (!isEqual(b.x,mid.x)) x = xSignB;
		
		if (!isEqual(a.y,mid.y)) y = ySignA;
		else if (!isEqual(b.y,mid.y)) y = ySignB;
		
		if (!isEqual(a.z,mid.z)) z = zSignA;
		else if (!isEqual(b.z,mid.z)) z = zSignB;
		
		if (x && y && z) WHD.add(t);
		else if (x && y && !z) WHZ.add(t);
		else if (x && !y && z) WYD.add(t);
		else if (x && !y && !z) WYZ.add(t);
		else if (!x && y && z) XHD.add(t);
		else if (!x && y && !z) XHZ.add(t);
		else if (!x && !y && z) XYD.add(t);
		else  XYZ.add(t);
	}
	
	private static boolean isValid(Vec3 vec) {
		if (vec == null) return false;
		float x = vec.x;
		float y = vec.y;
		float z = vec.z;
		
		if (x != x) return false;
		if (y != y) return false;
		if (z != z) return false;
		
		return true;
	}
	
	private void splitTriangle1X(Triangle t) {
		Vec3[] pnts = t.getPoints();
		Vec3 a = pnts[0];
		Vec3 b = pnts[1];
		Vec3 c = pnts[2];
		
		Vec3 mid = centre;
		Vec3 pn = new Vec3(1, 0, 0);
		Vec3[] v1 = null;
		Vec3[] v2 = null;
		
		if (isEqual(a.x, mid.x)) {
			Vec3 dirBC = c.subtract(b).normalize();
			float bcDotN = dirBC.dot(pn);
			float dBC = (mid.subtract(b).dot(pn)) / bcDotN;
			Vec3 vBC = b.add(dirBC.scale(dBC));
			
			v1 = new Vec3[]{a, vBC, c};
			v2 = new Vec3[]{a, b, vBC};
		}
		else if (isEqual(b.x, mid.x)) {
			Vec3 dirAC = a.subtract(b).normalize();
			float acDotN = dirAC.dot(pn);
			float dAC = (mid.subtract(a).dot(pn)) / acDotN;
			Vec3 vAC = a.add(dirAC.scale(dAC));
			
			v1 = new Vec3[]{vAC, b, c};
			v2 = new Vec3[]{a, b, vAC};
		}
		else if (isEqual(c.x, mid.x)) {
			Vec3 dirAB = a.subtract(b).normalize();
			float abDotN = dirAB.dot(pn);
			float dAB = (mid.subtract(a).dot(pn)) / abDotN;
			Vec3 vAB = a.add(dirAB.scale(dAB));
			
			v1 = new Vec3[]{vAB, c, a};
			v2 = new Vec3[]{vAB, b, c};
		}
		
		add(new Triangle(v1, null, null));
		add(new Triangle(v2, null, null));
		return;
	}
	
	private void splitTriangle1Y(Triangle t) {
		Vec3[] pnts = t.getPoints();
		Vec3 a = pnts[0];
		Vec3 b = pnts[1];
		Vec3 c = pnts[2];
		
		Vec3 mid = centre;
		Vec3 pn = new Vec3(0, 1, 0);
		Vec3[] v1 = null;
		Vec3[] v2 = null;
		
		if (isEqual(a.y, mid.y)) {
			Vec3 dirBC = c.subtract(b).normalize();
			float bcDotN = dirBC.dot(pn);
			float dBC = (mid.subtract(b).dot(pn)) / bcDotN;
			Vec3 vBC = b.add(dirBC.scale(dBC));
			
			v1 = new Vec3[]{a, vBC, c};
			v2 = new Vec3[]{a, b, vBC};
		}
		else if (isEqual(b.y, mid.y)) {
			Vec3 dirAC = a.subtract(b).normalize();
			float acDotN = dirAC.dot(pn);
			float dAC = (mid.subtract(a).dot(pn)) / acDotN;
			Vec3 vAC = a.add(dirAC.scale(dAC));
			
			v1 = new Vec3[]{vAC, b, c};
			v2 = new Vec3[]{a, b, vAC};
		}
		else if (isEqual(c.y, mid.y)) {
			Vec3 dirAB = a.subtract(b).normalize();
			float abDotN = dirAB.dot(pn);
			float dAB = (mid.subtract(a).dot(pn)) / abDotN;
			Vec3 vAB = a.add(dirAB.scale(dAB));
			
			v1 = new Vec3[]{vAB, c, a};
			v2 = new Vec3[]{vAB, b, c};
		}
		
		add(new Triangle(v1, null, null));
		add(new Triangle(v2, null, null));
		return;
	}
	
	private void splitTriangle1Z(Triangle t) {
		Vec3[] pnts = t.getPoints();
		Vec3 a = pnts[0];
		Vec3 b = pnts[1];
		Vec3 c = pnts[2];
		
		Vec3 mid = centre;
		Vec3 pn = new Vec3(0, 0, 1);
		Vec3[] v1 = null;
		Vec3[] v2 = null;
		
		if (isEqual(a.z, mid.z)) {
			Vec3 dirBC = c.subtract(b).normalize();
			float bcDotN = dirBC.dot(pn);
			float dBC = (mid.subtract(b).dot(pn)) / bcDotN;
			Vec3 vBC = b.add(dirBC.scale(dBC));
			
			v1 = new Vec3[]{a, vBC, c};
			v2 = new Vec3[]{a, b, vBC};
		}
		else if (isEqual(b.z, mid.z)) {
			Vec3 dirAC = a.subtract(b).normalize();
			float acDotN = dirAC.dot(pn);
			float dAC = (mid.subtract(a).dot(pn)) / acDotN;
			Vec3 vAC = a.add(dirAC.scale(dAC));
			
			v1 = new Vec3[]{vAC, b, c};
			v2 = new Vec3[]{a, b, vAC};
		}
		else if (isEqual(c.z, mid.z)) {
			Vec3 dirAB = a.subtract(b).normalize();
			float abDotN = dirAB.dot(pn);
			float dAB = (mid.subtract(a).dot(pn)) / abDotN;
			Vec3 vAB = a.add(dirAB.scale(dAB));
			
			v1 = new Vec3[]{vAB, c, a};
			v2 = new Vec3[]{vAB, b, c};
		}
		
		add(new Triangle(v1, null, null));
		add(new Triangle(v2, null, null));
		return;
	}
	
	private void splitTriangle2X(Triangle t) {
		splitTriangle(t, true, false, false);
	}
	
	private void splitTriangle2Y(Triangle t) {
		splitTriangle(t, false, true, false);
	}
	
	private void splitTriangle2Z(Triangle t) {
		splitTriangle(t, false, false, true);
	}
	
	public boolean collides(Vec3 mid, Vec3 sizeOfBox, Vec3 instancePosition) {
		float centreX = this.centre.x + instancePosition.x;
		float centreY = this.centre.y + instancePosition.y;
		float centreZ = this.centre.z + instancePosition.z;
		
		float aHalfSizeX = sizeOfBox.x;
		float aHalfSizeY = sizeOfBox.y;
		float aHalfSizeZ = sizeOfBox.z;
		
		float bHalfSizeX = size.x;
		float bHalfSizeY = size.y;
		float bHalfSizeZ = size.z;
		
		float aX1 = mid.x - aHalfSizeX;
		float aY1 = mid.y - aHalfSizeY;
		float aZ1 = mid.z - aHalfSizeZ;
		float aX2 = mid.x + aHalfSizeX;
		float aY2 = mid.y + aHalfSizeY;
		float aZ2 = mid.z + aHalfSizeZ;
		
		float bX1 = centreX - bHalfSizeX;
		float bY1 = centreY - bHalfSizeY;
		float bZ1 = centreZ - bHalfSizeZ;
		float bX2 = centreX + bHalfSizeX;
		float bY2 = centreY + bHalfSizeY;
		float bZ2 = centreZ + bHalfSizeZ;
		
		boolean x = (aX1 >= bX1 && aX1 <= bX2) || (aX2 >= bX1 && aX2 <= bX2) || (bX1 >= aX1 && bX1 <= aX2);
		boolean y = (aY1 >= bY1 && aY1 <= bY2) || (aY2 >= bY1 && aY2 <= bY2) || (bY1 >= aY1 && bY1 <= aY2);
		boolean z = (aZ1 >= bZ1 && aZ1 <= bZ2) || (aZ2 >= bZ1 && aZ2 <= bZ2) || (bZ1 >= aZ1 && bZ1 <= aZ2);
		
		return x && y && z;
	}
	
	public Vec3 getPosition() {
		return centre;
	}
	
	public Vec3 getSize() {
		return size;
	}
	
	public static CollisionEvent collides(Octree tree1, Vec3 pos1, Octree tree2, Vec3 pos2) {
		if (tree1 == null || tree2 == null) return null;
		v.set(tree2.getPosition());
		v.m_add(pos2);
		boolean collides = tree1.collides(v, tree2.getSize(), pos1);
		
		if (collides) {
			if (tree1.depth >= tree1.MAX_DEPTH) {
				boolean data1 = tree1.hasData;
				boolean data2 = tree2.hasData;
				
				if (data1 && data2) {
					return new CollisionEvent(tree1, tree2);
				}
				return null;
			}
			
			Octree[] children1 = tree1.children();
			Octree[] children2 = tree2.children();
			
			for (int i = 7; i >= 0; i--) {
				for (int j = 7; j >= 0; j--) {
					CollisionEvent test = collides(children1[i], pos1, children2[j], pos2);
					if (test != null) return test;
				}
			}
		}
		
		return null;
	}
	
	public static CollisionEvent collides(ArrayList<Triangle> trig1, ArrayList<Triangle>trig2) {
		Iterator<Triangle> list1 = trig1.iterator();
		Iterator<Triangle> list2 = trig2.iterator();
		
		while (list1.hasNext()) {
			Triangle t1 = list1.next();
			while (list2.hasNext()) {
				Triangle t2 = list2.next();
				
				/* Re-implement non-cheating version.
				 Good enough approx for now.*/
				if (t1.intersects(t2)) {
					return new CollisionEvent(t1, t2);
				}
				
				//return new CollisionEvent(t1, t2);
			}
		}
		
		return null;
	}
	
	public Octree[] children() {
		return new Octree[] {XYZ, WYZ, XHZ, XYD, WHZ, WYD, XHD, WHD};
	}
	
	public static Octree wrap(Model m) {
		return wrap(m, 3);
	}
	
	public static Octree wrap(Model m, int maxDepth) {
		VertexBuffer vertex = m.getBuffer();
		float[] vertexBuffer = vertex.getVertBuffer(0).array();
		int num = vertexBuffer.length;
		
		float minX = 10000, minY = 10000, minZ = 10000;
		float maxX = -10000, maxY = -10000, maxZ = -10000;
		for (int j = 0; j <= num-3; j += 3) {
			float x = vertexBuffer[j];
			float y = vertexBuffer[j+1];
			float z = vertexBuffer[j+2];
			
			if (x < minX) minX = x;
			if (y < minY) minY = y;
			if (z < minZ) minZ = z;
			
			if (x > maxX) maxX = x;
			if (y > maxY) maxY = y;
			if (z > maxZ) maxZ = z;
		}
		
		Vec3 min = new Vec3(minX, minY, minZ);
		Vec3 max = new Vec3(maxX, maxY, maxZ);
		
		min.m_add(max);
		min.m_scale(0.5f);
		Vec3 centre = min;
		
		max.m_subtract(centre);
		
		Octree tree = new Octree(maxDepth, centre, max);
		
		for (int j = 0; j <= num-8; j += 9) {
			float x1 = vertexBuffer[j];
			float y1 = vertexBuffer[j+1];
			float z1 = vertexBuffer[j+2];
			float x2 = vertexBuffer[j+3];
			float y2 = vertexBuffer[j+4];
			float z2 = vertexBuffer[j+5];
			float x3 = vertexBuffer[j+6];
			float y3 = vertexBuffer[j+7];
			float z3 = vertexBuffer[j+8];
			
			Vec3 p1 = new Vec3(x1, y1, z1);
			Vec3 p2 = new Vec3(x2, y2, z2);
			Vec3 p3 = new Vec3(x3, y3, z3);
			
			Triangle trig = new Triangle(new Vec3[] {p1, p2, p3}, null, null);
			tree.add(trig);
		}
		
		// Finalize it
		tree.finalizeTree();
		
		return tree;
	}
	
	private final void finalizeTree() {
		if (depth < MAX_DEPTH) {
			XYZ.finalizeTree();
			XYD.finalizeTree();
			XHZ.finalizeTree();
			XHD.finalizeTree();
			WYZ.finalizeTree();
			WYD.finalizeTree();
			WHZ.finalizeTree();
			WHD.finalizeTree();
		
			hasData = (XYZ.hasData || XYD.hasData || XHZ.hasData || XHD.hasData ||
				WYZ.hasData || WYD.hasData || WHZ.hasData || WHD.hasData);
		}
	}
	
	private final void splitTriangle(Triangle trig, boolean xChop, boolean yChop, boolean zChop) {
		Vec3[] pnts = trig.getPoints();
		Vec3 a = pnts[0];
		Vec3 b = pnts[1];
		Vec3 c = pnts[2];
		
		Vec3 p0 = centre;
		
		Vec3 dirAB = b.subtract(a).normalize();
		Vec3 dirAC = c.subtract(a).normalize();
		Vec3 dirBC = c.subtract(b).normalize();

		Vec3 pn = new Vec3(0, 0, 0);
		if (xChop) pn = new Vec3(1, 0, 0);
		else if (yChop) pn = new Vec3(0, 1, 0);
		else if (zChop) pn = new Vec3(0, 0, 1);
		
		float abDotN = dirAB.dot(pn);
		float acDotN = dirAC.dot(pn);
		float bcDotN = dirBC.dot(pn);
		float dAB = 0;
		float dAC = 0;
		float dBC = 0;
		if (abDotN != 0) dAB = ((p0.subtract(a)).dot(pn)) / abDotN;
		if (acDotN != 0) dAC = ((p0.subtract(a)).dot(pn)) / acDotN;
		if (bcDotN != 0) dBC = ((p0.subtract(b)).dot(pn)) / bcDotN;
		
		Vec3 vAB = null;
		Vec3 vAC = null;
		Vec3 vBC = null;
		
		if (abDotN != 0) vAB = a.add(dirAB.scale(dAB));
		if (acDotN != 0) vAC = a.add(dirAC.scale(dAC));
		if (bcDotN != 0) vBC = b.add(dirBC.scale(dBC));
		
		if (dAB < 0 || dAB > b.subtract(a).length()) vAB = null;
		if (dAC < 0 || dAC > c.subtract(a).length()) vAC = null;
		if (dBC < 0 || dBC > c.subtract(b).length()) vBC = null;

		Triangle t1 = null, t2 = null, t3 = null;
		
		if (vAB != null && vAC != null) {
			t1 = new Triangle(new Vec3[]{a, vAB, vAC}, null, null);
			t2 = new Triangle(new Vec3[]{b, vAC, vAB}, null, null);
			t3 = new Triangle(new Vec3[]{c, vAC, b}, null, null);
		}
		else if (vAB != null && vBC != null) {
			t1 = new Triangle(new Vec3[]{b, vAB, vBC}, null, null);
			t2 = new Triangle(new Vec3[]{c, vBC, a}, null, null);
			t3 = new Triangle(new Vec3[]{a, vAB, vBC}, null, null);
		}
		else if (vAC != null && vBC != null) {
			t1 = new Triangle(new Vec3[]{c, vAC, vBC}, null, null);
			t2 = new Triangle(new Vec3[]{b, vBC, vAC}, null, null);
			t3 = new Triangle(new Vec3[]{a, b, vAC}, null, null);
		}
		
		
		add(t1);
		add(t2);
		add(t3);
	}
}
