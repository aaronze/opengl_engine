package sagma.core.data.model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.render.Game;

public class VertexBuffer {
	FloatBuffer verticies;
	FloatBuffer normals;
	FloatBuffer texCoords;
	IntBuffer triangles;
	int[] buffer = new int[4];
	int vOffset;
	int nOffset;
	int tOffset;
	int stride;
	
	public static VertexBuffer interleave(float[] v, float[] n, float[] t, int[] vI, int[] nI, int[] tI) {
		// Leave data [v.x, v.y, v.z, n.x, n.y, n.z, t.u, t.v] = 8 * 4
		final int BYTES_PER_FLOAT = 4;
		final int FLOATS_PER_VERT = 3;
		final int DATA_PER_LEAF = 8;
		
		final int SIZE_OF_LEAF = DATA_PER_LEAF * BYTES_PER_FLOAT;
		
		final int size = v.length * DATA_PER_LEAF;
		final float[] leave = new float[size];
		
		for (int i = 0; i < vI.length; i++) {
			int vIndex = vI[i] * FLOATS_PER_VERT;
			int nIndex = nI[i] * FLOATS_PER_VERT;
			int tIndex = tI[i] * FLOATS_PER_VERT;
			
			int pos = vI[i] * DATA_PER_LEAF;
			
			leave[pos+0] = v[vIndex];
			leave[pos+1] = v[vIndex+1];
			leave[pos+2] = v[vIndex+2];
			
			if (n.length > 0) {
				leave[pos+3] = n[nIndex];
				leave[pos+4] = n[nIndex+1];
				leave[pos+5] = n[nIndex+2];
			}
			
			if (t.length > 0) {
				leave[pos+6] = t[tIndex];
				leave[pos+7] = t[tIndex+1];
			}
		}
		
		
		VertexBuffer buffer = new VertexBuffer();
		buffer.verticies = FloatBuffer.wrap(leave);
		buffer.triangles = IntBuffer.wrap(vI);
		buffer.vOffset = 0;
		buffer.nOffset = 3 * BYTES_PER_FLOAT;
		buffer.tOffset = 6 * BYTES_PER_FLOAT;
		buffer.stride = SIZE_OF_LEAF;
		buffer.compileInterleave();
		return buffer;
	}
	
	public static VertexBuffer wrap(float[] v, float[] n, float[] t, int[] f) {
		VertexBuffer buffer = new VertexBuffer();
		buffer.verticies = FloatBuffer.wrap(v);
		buffer.normals = FloatBuffer.wrap(n);
		buffer.texCoords = FloatBuffer.wrap(t);
		buffer.triangles = IntBuffer.wrap(f);
		buffer.compileWrapper();
		return buffer;
	}
	
	public void compileInterleave() {
		GLAutoDrawable drawable = Game.savedDrawable;
		GL2 gl = drawable.getGL().getGL2();
		
		int bufferSize = verticies.capacity() * 4;
		
		gl.glGenBuffers(1, buffer, 0);
		buffer[1] = buffer[0];
		buffer[2] = buffer[0];
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer[0]);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, bufferSize, verticies, GL.GL_STATIC_DRAW);
	}
	
	public void compileWrapper() {
		GLAutoDrawable drawable = Game.savedDrawable;
		GL2 gl = drawable.getGL().getGL2();
		
		int bufferSize = verticies.capacity() * 4;
		
		gl.glGenBuffers(4, buffer, 0);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer[0]);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, bufferSize, verticies, GL.GL_STATIC_DRAW);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer[1]);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, bufferSize, normals, GL.GL_STATIC_DRAW);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer[2]);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, bufferSize, texCoords, GL.GL_STATIC_DRAW);
	}
	
	public final void draw(GL2 gl) {	
		bindBuffers(gl);
	    gl.glDrawElements(GL.GL_TRIANGLES, triangles.capacity(), GL.GL_UNSIGNED_INT, triangles);
	}
	
	public void bindBuffers(GL2 gl) {
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer[0]);
		gl.glVertexPointer(3, GL.GL_FLOAT, stride, vOffset);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer[1]);
		gl.glNormalPointer(GL.GL_FLOAT, stride, nOffset);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer[2]);
		gl.glTexCoordPointer(2, GL.GL_FLOAT, stride, tOffset);
	}
}
