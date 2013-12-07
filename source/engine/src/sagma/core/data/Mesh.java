package sagma.core.data;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLPointerFunc;

import sagma.core.material.*;
import sagma.core.math.Triangle;
import sagma.core.math.Vec3;
import sagma.core.render.Game;

/**
 * Contains the raw information in a mesh.
 * If the data needs alteration then updateVerticies can be used.
 * 
 * @author Aaron Kison
 * @edited Wynand Marais
 */
public class Mesh {
	private Material material;
	public int[] iVertex;
	public int[] iTexture;
	public int[] iNormal;
	public FloatBuffer vboVertex;
	public FloatBuffer vboNormal;
	public FloatBuffer vboTexture;
	public int size;
	public int trigs;
	public boolean updateVertex;
	public boolean updateNormal;
	public boolean updateTexture;
	
	public Mesh() {
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		
		iVertex = new int[1];
		iNormal = new int[1];
		iTexture = new int[1];
		
		gl.glGenBuffers(1, iVertex, 0);
		gl.glGenBuffers(1, iNormal, 0);
		gl.glGenBuffers(1, iTexture, 0);
	}
	
	public Mesh getClone() {
		if (material == null) return null;
		Mesh mesh = new Mesh();
		mesh.build(material, vboVertex.duplicate().array(), vboNormal.duplicate().array(), vboTexture.duplicate().array());
		return mesh;
	}
	
	public FloatBuffer getVertexBuffer() {
		return vboVertex;
	}
	
	public FloatBuffer getNormalBuffer() {
		return vboNormal;
	}
	
	public FloatBuffer getTextureBuffer() {
		return vboTexture;
	}
	
	public int getSize() {
		return size;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void build(Material m, float[] v, float[] n, float[] t) {
		// Make sure the data arrays have the same number of components in them
		if(v.length/3 != n.length/3 || v.length/3 != t.length/2)
		{
			throw new RuntimeException("Error - Data length mismatch!");
		}
		
		// Are integer multiples of 3 for v and n, and multiples of 2 for t
		if(v.length % 3 != 0 || n.length % 3 != 0 || t.length % 2 != 0)
		{
			throw new RuntimeException("Error - Data multpiles are incorrect!");
		}
		
		
		// Are not empty.
		if(v.length == 0 || n.length == 0 || t.length == 0)
		{
			throw new RuntimeException("Error - Null data passed!");
		}
		
		if (m == null) m = SolidColor.WHITE;
		
		size = v.length;
		trigs = size / 3;
        material = m;
        
        GL2 gl = Game.savedDrawable.getGL().getGL2();

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, iVertex[0]);
        vboVertex = FloatBuffer.wrap(v);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, v.length * 4, vboVertex, GL.GL_STATIC_DRAW);     
        
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, iNormal[0]);
        vboNormal = FloatBuffer.wrap(n);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, n.length * 4, vboNormal, GL.GL_STATIC_DRAW);
        
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, iTexture[0]);
        vboTexture = FloatBuffer.wrap(t);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, t.length * 4, vboTexture, GL.GL_STATIC_DRAW);
	}
	
	public void draw(GL2 gl) {	
		activate(gl);
        
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, trigs);
        
        deactivate(gl);
	}
	
	public void activate(GL2 gl) {
		gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GLPointerFunc.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GLPointerFunc.GL_TEXTURE_COORD_ARRAY);
        
        if (material != null) material.activate(gl);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, iVertex[0]);
		if (updateVertex) {
			gl.glBufferData(GL.GL_ARRAY_BUFFER, size * 4, vboVertex, GL.GL_STATIC_DRAW);
			updateVertex = false;
		}
        gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, iNormal[0]);
        if (updateNormal) {
			gl.glBufferData(GL.GL_ARRAY_BUFFER, size * 4, vboNormal, GL.GL_STATIC_DRAW);
			updateNormal = false;
		}
        gl.glNormalPointer(GL.GL_FLOAT, 0, 0);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, iTexture[0]);
        if (updateTexture) {
			gl.glBufferData(GL.GL_ARRAY_BUFFER, size * 4, vboTexture, GL.GL_STATIC_DRAW);
			updateTexture = false;
		}
        gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, 0);
	}
	
	public void deactivate(GL2 gl) {
		if (material != null) material.deactivate(gl);
        
        gl.glDisableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GLPointerFunc.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GLPointerFunc.GL_TEXTURE_COORD_ARRAY);
	}
	
	public void dispose() {
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		
		gl.glDeleteBuffers(1, iVertex, 0);
		gl.glDeleteBuffers(1, iNormal, 0);
		gl.glDeleteBuffers(1, iTexture, 0);
		
		material = null;
		vboVertex = null;
		vboNormal = null;
		vboTexture = null;
		
		size = 0;
		trigs = 0;
	}
	
	public void updateVertex(float[] v) {
		vboVertex = FloatBuffer.wrap(v);
		updateVertex = true;
	}
	
	public void updateNormal(float[] n) {
		vboNormal = FloatBuffer.wrap(n);
		updateNormal = true;
	}
	
	public void updateTexture(float[] t) {
		vboTexture = FloatBuffer.wrap(t);
		updateTexture = true;
	}
	
	public Iterator<Triangle> iterator() {
    	ArrayList<Triangle> trigArray = new ArrayList<Triangle>();
    	
		float[] v = vboVertex.array();
		
		for (int k = 0; k < v.length; k += 9) {
			Triangle t = new Triangle(new Vec3[] {
					new Vec3(v[k], v[k+1], v[k+2]),
					new Vec3(v[k+3], v[k+4], v[k+5]),
					new Vec3(v[k+6], v[k+7], v[k+8])
			}, null, null);
			trigArray.add(t);
    		}
    	
    	return trigArray.iterator();
    }

	public void setMaterial(Material m) {
		material = m;
	}
}
