package sagma.games.rts.sfx;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import sagma.core.data.Mesh;
import sagma.core.data.VertexBuffer;
import sagma.core.data.generator.vector.VectorGenerator;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.model.MeshAnimator;
import sagma.core.model.MeshSkeleton;
import sagma.core.render.Render;

public class MeshExploder {
	public int DURATION = 4000;
	public Timer timer;
	
	public void explode(final Instance i) {
		VertexBuffer buf = i.model().getBuffer();
		for (int j = buf.getSize()-1; j >= 0; j--) {
			explode(buf.getMesh(j));
		}
		
		timer = new Timer(DURATION, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Render.remove(i);
				timer.stop();
			}
			
		});
		timer.start();
	}
	
	public void explode(Mesh mesh) {
		VectorGenerator movement = new VectorGenerator() {

			@Override
			public void setNextVector(Object caller, Vec3 vec) {
				Vec3 v = (Vec3)caller;
				float f = (float)Math.random()*-0.06f;
				vec.set(v);
				vec.m_normalize();
				vec.m_scale(f);
			}
			
		};
		
		MeshSkeleton skel = new MeshSkeleton(mesh, movement);
		MeshAnimator anim = new MeshAnimator();
		anim.setSolid(false);
		anim.add(skel);
		anim.setOffTimer(DURATION);
		Render.add(anim);
	}
}
