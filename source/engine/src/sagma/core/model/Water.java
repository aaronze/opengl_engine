package sagma.core.model;

import sagma.core.data.VertexBuffer;
import sagma.core.material.*;
import sagma.core.math.Triangle;
import sagma.core.math.Vec3;

public class Water {
	public static Model constructWater(Triangle[] trigs) {
		Material waterMaterial = new Shader("usemtl Shaders/water.vert");
		
		for (int i = 0; i < trigs.length; i++) {
			trigs[i].setMaterial(waterMaterial);
		}
		
		VertexBuffer buffer = new VertexBuffer();
		
		int VERTS_IN_TRIANGLE = 3;
		int FLOATS_IN_VERT = 3;
		float[] verts = new float[trigs.length * VERTS_IN_TRIANGLE * FLOATS_IN_VERT];
		
		for (int i = 0; i < trigs.length; i++) {
			int pos = i * VERTS_IN_TRIANGLE * FLOATS_IN_VERT;
			Triangle t = trigs[i];
			Vec3[] v = t.getPoints();
			
			verts[pos+0] = v[0].x;
			verts[pos+1] = v[0].y;
			verts[pos+2] = v[0].z;
			
			verts[pos+3] = v[1].x;
			verts[pos+4] = v[1].y;
			verts[pos+5] = v[1].z;
			
			verts[pos+6] = v[2].x;
			verts[pos+7] = v[2].y;
			verts[pos+8] = v[2].z;
		}
		
		buffer.buildVertexBufferSet(0, waterMaterial, verts);
		Model model = new Model(buffer);
		
		return model;
	}
}
