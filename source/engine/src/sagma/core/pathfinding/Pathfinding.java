package sagma.core.pathfinding;

import java.util.ArrayList;

import sagma.core.math.LineSegment;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;

/**
 * <p>
 * Finds a path (optimal not guaranteed) between two given points and
 * given obstructions.
 * </p>
 * 
 * <p>
 * For best results the following should be kept in mind:
 * <ul>
 * <li>This algorithm is better suited to wide open spaces
 * <li>Complicated maze-like obstructions may result in incorrect results
 * <li>The design is for fast pathfinding and not accurate
 * </ul>
 * 
 * @author Aaron Kison
 *
 */
public class Pathfinding extends Thread {
	private Vec3 start, end;
	private ArrayList<Instance> objects;
	private ArrayList<LineSegment> path;
	private boolean isReady = false;
	int counter = 0;
	
	public Pathfinding(Vec3 start, Vec3 end) {
		this.start = start;
		this.end = end;
		
		objects = new ArrayList<Instance>();
		path = new ArrayList<LineSegment>();
	}
	
	public void add(Instance i) {
		objects.add(i);
	}
	
	public void add(Instance[] i) {
		for (Instance j : i)
			add(j);
	}
	
	public void add(ArrayList<Instance> list) {
		for (Instance i : list)
			add(i);
	}
	
	@Override
	public void run() {
		add(start, end);
		optimize();
		isReady = true;
	}
	
	public void optimize() {
		// Convert lines to points
		ArrayList<Vec3> points = new ArrayList<Vec3>(path.size()+1);
		points.add(path.get(0).a);
		for (LineSegment p : path) {
			points.add(p.b);
		}
		
		// Optimize
		ArrayList<Vec3> opt = new ArrayList<Vec3>(points.size());
		opt.add(points.get(0));
		int current = 0;
		int next = 1;
		int size = points.size();
		
		while (next < size-1) {
			Vec3 a = points.get(current);
			Vec3 b = points.get(next);
			
			while (isValid(a, b) && next < size-1) {
				next++;
				b = points.get(next);
			}
			
			if (current+1 == next) {
				current++;
				next++;
			} else {
				opt.add(points.get(next-1));
				current = next-1;
			}
		}
		
		opt.add(points.get(size-1));
		
		path = new ArrayList<LineSegment>();
		size = opt.size();
		
		for (int i = 0; i < size-1; i++) {
			path.add(new LineSegment(opt.get(i), opt.get(i+1)));
		}
	}
	
	public ArrayList<LineSegment> getPath() {
		return path;
	}
	
	public void add(Vec3 a, Vec3 b) {
		if (!isValid(a, b)) {
			Vec3 c = mid(a, b);
			boolean ac = isValid(a, c);
			boolean bc = isValid(c, b);
			
			if (!ac && !bc) {
				skid(a, c, b);
			} else if (bc) {
				add(a, c);
				addPath(c, b);
			} else if (ac) {
				add(c, b);
				addPath(a, c);
			}
		} else {
			addPath(a, b);
		}
	}
	
	private void skid(Vec3 a, Vec3 ab, Vec3 b) {
		Vec3 dir = ab.subtract(a).normalize();
		
		// TODO 3D Rotation
		float y = dir.y;
		dir.y = -dir.x;
		dir.x = y;
		
		float dirD = dir.length();
		
		Vec3 p = ab;
		while (!isValid(p)) {
			dirD *= 1.5f;
			p = ab.add(dir.scale(dirD));
		}
		
		add(a, p);
		add(p, b);
	}
	
	private static Vec3 mid(Vec3 a, Vec3 b) {
		return a.add(b).scale(0.5f);
	}
	
	private boolean isValid(Vec3 a, Vec3 b) {
		for (Instance i : objects) {
			if (Pathfinding.collides(i, a, b)) return false;
		}
		return true;
	}
	
	private boolean isValid(Vec3 v) {
		for (Instance i : objects) {
			if (Pathfinding.collides(i, v)) return false;
		}
		return true;
	}
	
	private void addPath(Vec3 a, Vec3 b) {
		path.add(new LineSegment(a, b));
	}
	
	public boolean isReady() {
		return isReady;
	}
	
	public final static boolean collides(Instance i, Vec3 lineA, Vec3 lineB) {
		Vec3 lDir = lineB.subtract(lineA).normalize();
		float lLen = lineB.subtract(lineA).length();
		
		float pLen = i.getPosition().subtract(lineA).length();
		if (pLen < 0 || pLen > lLen) return false;
		
		Vec3 p = lDir.scale(pLen).add(lineA);
		
		return Pathfinding.collides(i, p);
	}
	
	public final static boolean collides(Instance i, Vec3 v) {
		return v.lengthSquared() < i.model().getBounds().rSquared;
	}
}
