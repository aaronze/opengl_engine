package sagma.core.data.model;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import sagma.core.data.Octree;
import sagma.core.model.bounding.BoundingSphere;

public class Model {
	private ArrayList<Mesh> mesh;
	private Octree octree;
	private BoundingSphere bounding;
	
	public Model() {
		mesh = new ArrayList<Mesh>();
	}
	
	public void add(Mesh m) {
		mesh.add(m);
	}
	
	public void add(Octree tree) {
		octree = tree;
	}
	
	public void add(BoundingSphere sphere) {
		bounding = sphere;
	}
	
	public void draw(GL2 gl) {
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		for (Mesh m : mesh) m.draw(gl);
				
		gl.glDisable(GL.GL_DEPTH_TEST);
	}
	
	public Octree getOctree() {
		return octree;
	}
	
	public BoundingSphere getBounds() {
		return bounding;
	}
}
