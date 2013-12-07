package sagma.core.data.generator.planetoid;

import java.util.ArrayList;

import sagma.core.data.Color4f;
import sagma.core.data.PointOctree;
import sagma.core.data.generator.field.ImageFieldGenerator;
import sagma.core.data.model.Model;
import sagma.core.material.Material;
import sagma.core.material.SolidColor;
import sagma.core.math.Triangle;
import sagma.core.math.Vec3;
import sagma.core.model.Constructable;
import sagma.core.model.ModelConstructor;

public abstract class PlanetoidGenerator implements Constructable {
	public PlanetoidGenerator generateNextPlanetoid() {
		return generateNextPlanetoid(1.0f);
	}
	
	public final static int DEFAULT_DIVISIONS = 5;
	private static SolidColor DEFAULT_SHADER = new SolidColor(Color4f.WHITE);
	protected int divisions;
	protected float radius;
	protected Material material = DEFAULT_SHADER;
	protected int count;
	protected ModelConstructor maker;
	protected Model model;
	protected ArrayList<Trig> triangles;
	protected Vec3[] verts;
	protected int index = 0;
	protected int size;
	protected PointOctree vertTree;
	
	public PlanetoidGenerator(String name, int divisions) {
		if (divisions >= 8) {
			System.out.println("ERROR: Planetoid Division count of 8 is too high, setting to default of 7.");
			divisions = 7;
		}
		this.divisions = divisions;
		maker = new ModelConstructor(name);
	}
	
	public PlanetoidGenerator(String name) {
		divisions = DEFAULT_DIVISIONS;
		maker = new ModelConstructor(name);
	}
	
	/**
	 * @param vec Direction of point on the sphere for deterministic material selection 
	 */
	public Material getMaterial(Vec3[] vec) {
		return material;
	}
	
	public PlanetoidGenerator generateNextPlanetoid(float scale) {
		radius = scale;
		
		size = 8 * (int)Math.pow(4, divisions);
		verts = new Vec3[size];
		triangles = new ArrayList<Trig>(size);
		index = 0;
		vertTree = new PointOctree();
		
		// Start with the poles
		int UP = addVector(new Vec3(0, radius, 0));
		int DOWN = addVector(new Vec3(0, -radius, 0));
		int RIGHT = addVector(new Vec3(radius, 0, 0));
		int LEFT = addVector(new Vec3(-radius, 0, 0));
		int FORWARD = addVector(new Vec3(0, 0, radius));
		int BACK = addVector(new Vec3(0, 0, -radius));
		
		addTriangle(UP, FORWARD, LEFT);
		addTriangle(UP, RIGHT, FORWARD);
		addTriangle(UP, LEFT, BACK);
		addTriangle(UP, BACK, RIGHT);
		addTriangle(DOWN, FORWARD, RIGHT);
		addTriangle(DOWN, LEFT, FORWARD);
		addTriangle(DOWN, RIGHT, BACK);
		addTriangle(DOWN, BACK, LEFT);
		
		// Iterate through the divisions
		for (int i = 0; i < divisions; i++) {
			subdivide();
		}
		modify();
		
		// Add the triangles to the maker
		for (Trig trig : triangles) {
			addTriangle(trig);
		}
		
		Vec3[] norms = new Vec3[verts.length];
		Vec3[] tex = new Vec3[verts.length];
		for (int i = 0; i < verts.length; i++) {
			Vec3 vert = verts[i];
			if (vert != null) {
				norms[i] = vert.normalize();
				tex[i] = getUVOfVector(norms[i]);
			}
		}
		
		int[] indexArray = new int[triangles.size() * 3];
		for (int i = 0; i < triangles.size(); i++) {
			int pos = i * 3;
			Trig trig = triangles.get(i);
			indexArray[pos] = trig.a;
			indexArray[pos+1] = trig.b;
			indexArray[pos+2] = trig.c;
		}

		return this;
	}
	
	protected void build() {
		maker.build();
	}
	
	protected void addTriangle(Trig trig) {
		Vec3[] vertex = new Vec3[] {
				verts[trig.a], 
				verts[trig.b],
				verts[trig.c]
		};
		Vec3[] norm = new Vec3[] {
				vertex[0].normalize(),
				vertex[1].normalize(),
				vertex[2].normalize()
		};
		Vec3[] tex = new Vec3[] {
				getUVOfVector(norm[0]),
				getUVOfVector(norm[1]),
				getUVOfVector(norm[2])
		};
		maker.setMaterial(getMaterial(vertex));
		maker.add(new Triangle(vertex, norm, tex));
	}
	
	private static Vec3 getUVOfVector(Vec3 vec) {
		return ImageFieldGenerator.getUVOfVector(vec);
	}
	
	protected int addVector(Vec3 v) {
		int vI = vertTree.find(v);
		if (vI != -1) return vI;
		
		int i = index;
		vertTree.add(v, i);
		verts[index++] = v;
		return i;
	}
	
	protected void addTriangle(int a, int b, int c) {
		triangles.add(new Trig(a, b, c));
	}
	
	protected void subdivide() {
		// For every triangle in the list, add 3 new points at each midpoint of each edge
		// And create 3 new triangles
		
		int trigSize = triangles.size();
		ArrayList<Trig> newTriangles = new ArrayList<Trig>(trigSize);
		for (int i = trigSize-1; i >= 0; i--) {
			Trig triangle = triangles.get(i);
			
			int A = triangle.a;
			int B = triangle.b;
			int C = triangle.c;
			
			int AB = addVector(mid(verts[A], verts[B]));
			int BC = addVector(mid(verts[B], verts[C]));
			int AC = addVector(mid(verts[A], verts[C]));
			
			newTriangles.add(new Trig(A, AB, AC));
			newTriangles.add(new Trig(B, BC, AB));
			newTriangles.add(new Trig(C, AC, BC));
			newTriangles.add(new Trig(AC, AB, BC));
		}
		triangles = null;
		triangles = newTriangles;
	}
	
	protected abstract void modify();
	
	private final static Vec3 mid(Vec3 a, Vec3 b) {
		Vec3 v = a.add(b);
		v.m_scale(0.5f);
		return v;
	}
	
	public void setMaterial(Material m) {
		material = m;
	}
	
	@Override
	public ModelConstructor getModelConstructor() {
		return maker;
	}
	
	class Trig {
		int a, b, c;
		
		public Trig(int a, int b, int c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}
	}
	
	public Model getModel() {
		return model;
	}
}
