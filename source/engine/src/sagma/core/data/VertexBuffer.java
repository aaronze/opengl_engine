package sagma.core.data;

/**
 *
 * @author Aaron Kison
 */
import javax.media.opengl.*;

import java.nio.FloatBuffer;

import sagma.core.material.Material;
import sagma.core.math.Vec3;

public class VertexBuffer {
    private Mesh[] mesh;

    public VertexBuffer() {
        mesh = new Mesh[1];
        init();
    }
    
    public VertexBuffer getClone() {
    	VertexBuffer vb = new VertexBuffer(mesh.length);
    	for (int i = 0; i < 1; i++) {
    		vb.setMesh(mesh[i].getClone(), i);
    	}
    	
    	return vb;
    }

    public VertexBuffer(int size) {
        mesh = new Mesh[size];
        init();
    }

    private void init() {
        for (int i = mesh.length-1; i >= 0; i--) {
        	mesh[i] = new Mesh();
        }
    }
    
    public Mesh getMesh(int i) {
    	return mesh[i];
    }
    
    public void setMesh(Mesh m, int i) {
    	mesh[i] = m;
    }

    /**
    Constructs a set of vertex buffer objects using the given verticies.
    The normals are created automatically using the given information, but it is far from accurate and just a gross approximation.
    The texture mapping is left as default, which is fair enough
     */
    public void buildVertexBufferSet(int set, Material material, float[] vertexBuffer) {
        int length = vertexBuffer.length;
        float[] normalBuffer = new float[length];
        int normalCounter = 0;

        for (int i = 0; i < vertexBuffer.length - 9; i += 9) {
            Vec3[] verticies = new Vec3[3];
            int vertexCounter = 0;
            for (int vertexIndex = i; vertexIndex < i + 9; vertexIndex += 3) {
                verticies[vertexCounter++] = new Vec3(vertexBuffer[vertexIndex], vertexBuffer[vertexIndex + 1], vertexBuffer[vertexIndex + 2]);
            }
            // Normal = (V1 - V0) x (V2 - V0)
            Vec3 leftSide = verticies[1].subtract(verticies[0]);
            Vec3 rightSide = verticies[2].subtract(verticies[0]);
            Vec3 result = leftSide.cross(rightSide);

            for (int j = 0; j < 3; j++) {
                normalBuffer[normalCounter++] = result.x;
                normalBuffer[normalCounter++] = result.y;
                normalBuffer[normalCounter++] = result.z;
            }
        }

        buildVertexBufferSet(set, material, vertexBuffer, normalBuffer);
    }

    /**
    Constructs a set of vertex buffer objects using the given verticies and normals.
    The texture mapping is left as default, which is fair enough
    @param set The set number, each buffer needs a increment in set.
     */
    public void buildVertexBufferSet(int set, Material material, float[] vertexBuffer, float[] normalBuffer) {
        int length = vertexBuffer.length;
        float[] textureBuffer = new float[length * 4];
        for (int k = 0; k < textureBuffer.length - 8; k += 8) {
            for (int l = k + 3; l <= k + 6; l++) {
                textureBuffer[l] = 1;
            }
        }
        buildVertexBufferSet(set, material, vertexBuffer, normalBuffer, textureBuffer);
    }

    /**
    Constructs a new set of vertex buffer objects using the given verticies, normals and a set of texture mapping.
    I hope it's a good reason to be overriding the default texture mapping, mostly it's better left default.
     */
    public void buildVertexBufferSet(int set, Material material, float[] vertexBuffer, float[] normalBuffer, float[] textureBuffer) {
        mesh[set].build(material, vertexBuffer, normalBuffer, textureBuffer);
    }
    
    public FloatBuffer getVertBuffer(int set) {
    	return mesh[set].getVertexBuffer();
    }
    
    public FloatBuffer getNormBuffer(int set) {
    	return mesh[set].getNormalBuffer();
    }
    
    public FloatBuffer getTexBuffer(int set) {
    	return mesh[set].getTextureBuffer();
    }

    public int getBufferSize(int i) {
        return mesh[i].getSize();
    }

    public void dispose() {
        for (int i = mesh.length-1; i >= 0; i--) {
        	mesh[i].dispose();
        }
    }

    public Material getMaterial(int i) {
    	return mesh[i].getMaterial();
    }
    
    public void draw(GL2 gl) {
    	for (int i = mesh.length-1; i >= 0; i--) {
    		mesh[i].draw(gl);
    	}
    }
    
    public int getSize() {
    	return mesh.length;
    }
    
    public void setMaterial(Material m) {
    	for (int i = 0; i < mesh.length; i++) {
    		mesh[i].setMaterial(m);
    	}
    }
}
