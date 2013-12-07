package sagma.core.data.model;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import sagma.core.material.Material;

public class Mesh {
	private Material material;
	private ArrayList<PolygonBuffer> buffers;
	private boolean hasMask;
	
	public Mesh(Material mat) {
		material = mat;
		hasMask = material != null && material.hasMask();
		buffers = new ArrayList<PolygonBuffer>();
	}
	
	public void addBuffer(PolygonBuffer buffer) {
		buffers.add(buffer);
	}
	
	public final void draw(GL2 gl) {
		gl.glDisable(GL.GL_BLEND);
		if (material != null) {
			if (hasMask) material.activateMask(gl);
	        else material.activate(gl); 
		}
		
		for (PolygonBuffer pb : buffers) pb.draw(gl);
		
		if (material != null) {
			if (hasMask) material.deactivateMask(gl);
	        else material.deactivate(gl);
		}
		gl.glEnable(GL.GL_BLEND);
	}
}
