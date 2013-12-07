package sagma.core.model;

import java.util.ArrayList;
import java.util.Iterator;

import sagma.core.data.Mesh;
import sagma.core.data.generator.vector.VectorGenerator;
import sagma.core.math.Triangle;
import sagma.core.math.Vec3;
import sagma.core.render.Steppable;

public class MeshSkeleton implements Steppable {
	private ArrayList<MeshSkeleton> children;
	private VectorGenerator movement;
	private Mesh buffer;
	
	public MeshSkeleton(Mesh buffer, VectorGenerator movement) {
		children = new ArrayList<MeshSkeleton>();
		
		this.buffer = buffer;
		this.movement = movement;
	}

	@Override
	public void step() {
		move(movement);
		
		Iterator<MeshSkeleton> list = children.iterator();
		
		while (list.hasNext()) {
			MeshSkeleton child = list.next();
			child.move(movement);
			child.step();
		}
	}
	
	public void move(VectorGenerator num) {
		Iterator<Triangle> trigs = buffer.iterator();
		float[] v = new float[buffer.getSize()];
		int index = 0;
		
		Vec3 move = new Vec3(0,0,0);
		while (trigs.hasNext()) {
			Triangle t = trigs.next();
			
			Vec3[] pnts = t.getPoints();
			Vec3 v1 = pnts[0];
			Vec3 v2 = pnts[1];
			Vec3 v3 = pnts[2];
			
			num.setNextVector(v1, move);
			v1.m_add(move);
			v2.m_add(move);
			v3.m_add(move);
			
			v[index] = v1.x;
			v[index+1] = v1.y;
			v[index+2] = v1.z;
			v[index+3] = v2.x;
			v[index+4] = v2.y;
			v[index+5] = v2.z;
			v[index+6] = v3.x;
			v[index+7] = v3.y;
			v[index+8] = v3.z;
			
			index += 9;
		}
		
		buffer.updateVertex(v);
	}
	
	
}
