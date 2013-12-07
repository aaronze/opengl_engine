package sagma.games.rts.sfx;


import javax.swing.Timer;

import sagma.core.data.Mesh;
import sagma.core.data.VertexBuffer;
import sagma.core.data.generator.number.RandomNumberGenerator;
import sagma.core.data.generator.vector.VectorGenerator;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.model.MeshAnimator;
import sagma.core.model.MeshSkeleton;
import sagma.core.render.Render;

public class MeshJitter {
	private RandomNumberGenerator random = new RandomNumberGenerator(-0.005f, 0.005f);
	public Timer timer;
	
	public void explode(final Instance i) {
		VertexBuffer buf = i.model().getBuffer();
		for (int j = buf.getSize()-1; j >= 0; j--) {
			explode(buf.getMesh(j));
		}
	}
	
	public float rand() {
		return random.nextNumber();
	}
	
	public void explode(Mesh mesh) {
		VectorGenerator movement = new VectorGenerator() {

			@Override
			public void setNextVector(Object caller, Vec3 vec) {
				Vec3 v = (Vec3)caller;
				vec.set(v);
				vec.m_normalize();
				vec.m_scale(rand());
			}
			
		};
		
		MeshSkeleton skel = new MeshSkeleton(mesh, movement);
		MeshAnimator anim = new MeshAnimator();
		anim.setSolid(false);
		anim.add(skel);
		Render.add(anim);
	}
}
