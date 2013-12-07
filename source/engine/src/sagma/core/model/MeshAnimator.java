package sagma.core.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Timer;

import sagma.core.render.Render;

public class MeshAnimator extends Instance {
	ArrayList<MeshSkeleton> skeletons;
	Timer offTimer;
	
	public MeshAnimator() {
		skeletons = new ArrayList<MeshSkeleton>();
	}
	
	public void setOffTimer(int time) {
		offTimer = new Timer(time, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				skeletons = null;
				Render.remove(this);
				offTimer.stop();
			}
			
		});
		offTimer.start();
	}
	
	public void add(MeshSkeleton skeleton) {
		skeletons.add(skeleton);
	}
	
	@Override
	public void heartbeat() {
		if (skeletons == null) return;
		Iterator<MeshSkeleton> list = skeletons.iterator();
		while (list.hasNext()) {
			list.next().step();
		}
	}
}
