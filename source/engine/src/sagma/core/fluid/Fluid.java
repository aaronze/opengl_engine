package sagma.core.fluid;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.material.Material;
import sagma.core.math.ModelDimension;
import sagma.core.math.Triangle;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;

/**
 * A fluid is made up of many points that keep a minumum distance away from each other.
 * If a point is unable to move this creates a pressure.
 * 
 * After the point location is calculated a fluid can be rendered by asking for a 
 *  renderable version, this tethers the points together into a blob.
 *  
 * Blobs are formed by seperating the fluid points into distinct disconnected blobs,
 *  then creating the outside triangles of the blobs and smoothing using sub-division.
 * 
 * @author Aaron Kison
 *
 */
public class Fluid extends Instance {
	public float DENSITY = 0.2f;
	public float VISCOSITY = 0.001f;
	public float FRICTION_FACTOR = 0.1f;
	public float SMOOTH_SIZE = 0.1f;
	private float[] x;
	private float[] y;
	private float[] z;
	private float[] xSpeed;
	private float[] ySpeed;
	private float[] zSpeed;
	private boolean[] mask;
	private int max;
	private boolean isSimulated;
	private ModelDimension bounds;
	private Material material;
	
	private ArrayList<ArrayList<Vec3>> blobs;
	private ArrayList<Triangle> trigs;
	
	public Fluid(int maxPoints) {
		max = maxPoints;
		x = new float[max];
		y = new float[max];
		z = new float[max];
		xSpeed = new float[max];
		ySpeed = new float[max];
		zSpeed = new float[max];
		mask = new boolean[max];
		isSimulated = false;
	}
	
	public void setMaterial(Material m) {
		material = m;
	}
	
	public boolean addFluidPoint(Vec3 point, Vec3 speed) {
		for (int i = 0; i < max; i++) {
			if (!mask[i]) {
				mask[i] = true;
				x[i] = point.x;
				y[i] = point.y;
				z[i] = point.z;
				xSpeed[i] = speed.x;
				ySpeed[i] = speed.y;
				zSpeed[i] = speed.z;
				return true;
			}
 		}
		return false;
	}
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		if (material != null) material.activate(gl);
		
		gl.glPushMatrix();
		Vec3 pos = getPosition();
		gl.glTranslatef(pos.x, pos.y, pos.z);
		gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
		
		gl.glBegin(GL.GL_TRIANGLE_FAN);
		for (int i = 0; i < max; i++) {
			if (mask[i]) {
				gl.glVertex3f(x[i], y[i], z[i]);
			}
		}
		gl.glEnd();
		
		gl.glPopMatrix();
		
		if (material != null) material.deactivate(gl);
	}
	
	@Override
	public void step() {
		if (isSimulated) {
			for (int i = 0; i < max; i++) {
				compact();
				
				if (mask[i]) {
					float posX = x[i];
					float posY = y[i];
					float posZ = z[i];
					float sX = xSpeed[i];
					float sY = ySpeed[i];
					float sZ = zSpeed[i];
					ySpeed[i] -= 0.01f;
					
					if (posX + sX > bounds.maxX || posX + sX < bounds.minX) xSpeed[i] = -sX; 
					else x[i] += xSpeed[i] * FRICTION_FACTOR;
					
					if (posY + sY > bounds.maxY || posY + sY < bounds.minY) ySpeed[i] = -sY;
					else y[i] += ySpeed[i] * FRICTION_FACTOR;
					
					if (posZ + sZ > bounds.maxZ || posZ + sZ < bounds.minZ) zSpeed[i] = -sZ;
					else z[i] += zSpeed[i] * FRICTION_FACTOR;	
				}
			}
			
			blobify();
		}
	}
	
	private void compact() {
		Vec3 flee = new Vec3(0, 0, 0);
		Vec3 dir = new Vec3(0, 0, 0);
		for (int i = 0; i < max; i++) {
			if (!mask[i]) continue;
			flee.set(0, 0, 0);
			for (int j = 0; j < max; j++) {
				if (!mask[j]) continue;
				if (isClose(i, j)) {
					dir.set(x[i]-x[j], y[i]-y[j], z[i]-z[j]);
					if (dir.lengthSquared() > 0) {
						dir.m_normalize();
						flee.m_add(dir);
						flee.m_normalize();
					}
				}
			}
			float fleeLen = flee.lengthSquared();
			if (fleeLen < 0.9f || fleeLen > 1.1f) continue;
			flee.m_normalize();
			flee.m_scale(VISCOSITY);
			flee(i, flee);
		}
	}
	
	private void flee(int i, Vec3 dir) {
		xSpeed[i] += dir.x;
		ySpeed[i] += dir.y;
		zSpeed[i] += dir.z;
	}
	
	private boolean isClose(int i, int j) {
		float xD = x[i] - x[j];
		float yD = y[i] - y[j];
		float zD = z[i] - z[j];
		float dist = xD*xD + yD*yD + zD*zD;
		return dist < DENSITY*DENSITY;
	}
	
	public void setBounds(Vec3 min, Vec3 size) {
		bounds = new ModelDimension(min.x, min.y, min.z, size.x-min.x, size.y-min.y, size.z-min.z);
	}
	
	public void start() {
		isSimulated = true;
	}
	
	public void stop() {
		isSimulated = false;
	}
	
	private void blobify() {
		blobs = new ArrayList<ArrayList<Vec3>>();
		
		pointLoop:
		for (int i = 0; i < max; i++) {
			if (mask[i]) {
				Vec3 point = new Vec3(x[i], y[i], z[i]);
				for (ArrayList<Vec3> blob : blobs) {
					if (inRange(blob, point)) {
						blob.add(point);
						continue pointLoop;
					}
				}
				ArrayList<Vec3> blob = new ArrayList<Vec3>();
				blob.add(point);
				blobs.add(blob);
			}
		}
	}
	
	private boolean inRange(ArrayList<Vec3> points, Vec3 p) {
		for (Vec3 v : points) {
			if (v.distanceTo(p) > DENSITY) return false;
		}
		return true;
	}
	
	private void triangulate() {
		for (ArrayList<Vec3> blob : blobs) {
			for (float xi = -1; xi <= 1; xi += SMOOTH_SIZE) {
				for (float yi = -1; yi <= 1; yi += SMOOTH_SIZE) {
					for (float zi = -1; zi <= 1; zi += SMOOTH_SIZE) {
						Vec3 best = null;
						float bestLen = 0.0f;
						Vec3 p = new Vec3(xi, yi, zi);
						if (p.length() < 1) continue;
						
						Vec3[] v = blob.toArray(new Vec3[1]);
						for (Vec3 b : v) {
							float dot = b.dot(p);
							if (dot < SMOOTH_SIZE) {
								blob.remove(b);
								if (best == null) {
									best = b;
									bestLen = best.lengthSquared();
								} else {
									float len = b.lengthSquared();
									if (len > bestLen) {
										bestLen = len;
										best = b;
									}
								}
							}
						}
						blob.add(best);
					}
				}
			}
		}
		
		for (ArrayList<Vec3> blob : blobs) {
			
		}
	}
}
