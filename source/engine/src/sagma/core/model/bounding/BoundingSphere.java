package sagma.core.model.bounding;

import sagma.core.data.VertexBuffer;
import sagma.core.data.model.Collidable;
import sagma.core.math.Vec3;
import sagma.core.model.Model;

public class BoundingSphere {
	private float baseR;
	public float r;
	public float scale = 1.0f;
	public float rSquared;
	
	public static BoundingSphere wrap(Model model) {
		VertexBuffer vertex = model.getBuffer();
		float[] vertexBuffer = vertex.getVertBuffer(0).array();
		int num = vertexBuffer.length;
		
		float maxDistSquared = 0;
		for (int j = 0; j < num; j += 3) {
			float x = vertexBuffer[j];
			float y = vertexBuffer[j+1];
			float z = vertexBuffer[j+2];
			float distSquared = x*x + y*y + z*z;
			if (distSquared > maxDistSquared) {
				maxDistSquared = distSquared;
			}
		}
		
		float maxDist = (float)Math.sqrt(maxDistSquared);
		// Scale maxDist with model size
		
		BoundingSphere sphere = new BoundingSphere(maxDist, model.getScale().x);
		return sphere;
	}
	
	public void instanceGotScaled(float f) {
		scale = f;
		r = baseR * scale;
		rSquared = r*r;
	}
	
	public BoundingSphere(float size, float scale) {
		baseR = size;
		r = baseR * scale;
		rSquared = r*r;
	}
	
	public static boolean collides(Collidable i, Collidable j) {
		BoundingSphere sphere1 = i.getBounding();
		BoundingSphere sphere2 = j.getBounding();
		
		if (sphere1 == null || sphere2 == null) return false;
		
		Vec3 pos1 = i.getLocation();
		Vec3 pos2 = j.getLocation();
		
		float iScale1 = i.getScale();
		float iScale2 = j.getScale();
		
		float rad1 = sphere1.baseR * iScale1;
		float rad2 = sphere2.baseR * iScale2;
		
		float addR = rad1*rad1 + rad2*rad2;
		
		float x = pos1.x - pos2.x;
		float y = pos1.y - pos2.y;
		float z = pos1.z - pos2.z;
		
		return x*x+y*y+z*z < addR;
	}
}
