package sagma.core.animator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import sagma.core.data.generator.vector.ConstantVectorGenerator;
import sagma.core.data.generator.vector.VectorGenerator;
import sagma.core.math.Vec2;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;

public class InstanceRotater {
	private Instance instance;
	private Timer timer;
	private ArrayList<VectorGenerator> movements;
	private Vec3 vec = new Vec3(0,0,0);
	
	public InstanceRotater(Instance i) {
		instance = i;
		
		movements = new ArrayList<VectorGenerator>();
	}
	
	public void start() {
		timer = new Timer(40, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				moveStep();
			}
		});
		timer.start();
	}
	
	public void stop() {
		timer.stop();
		instance = null;
		movements = null;
	}
	
	void moveStep() {
		if (instance == null) {
			stop();
			return;
		}
		for (VectorGenerator gen : movements) {
			gen.setNextVector(this, vec);
			instance.addRotation(vec.x, vec.y, vec.z);
		}
	}
	
	public void addMovement(VectorGenerator vg) {
		movements.add(vg);
	}
	
	public void addMovement(Vec3 v) {
		movements.add(new ConstantVectorGenerator(v));
	}
	
	public void addMovement(float x, float y, float z) {
		addMovement(new Vec3(x, y, z));
	}
	
	public void addMovement(float x, float y) {
		addMovement(new Vec3(x, y, 0));
	}
	
	public void addMovement(float x) {
		addMovement(new Vec3(x, 0, 0));
	}
	
	public void addMovement(Vec2 v) {
		addMovement(new Vec3(v, 0));
	}
}
