package sagma.core.light;

import java.util.ArrayList;

import javax.media.opengl.GL2;

import sagma.core.math.Vec3;
import sagma.core.render.Game;

public class LightManager {
	private final ArrayList<VirtualLight> lights = new ArrayList<VirtualLight>();
	private VLIndex[] index = new VLIndex[8];
	
	public void add(VirtualLight light) {
		lights.add(light);
	}
	
	public void remove(VirtualLight light) {
		lights.remove(light);
	}
	
	public void activateLightsForPosition(Vec3 position) {
		clearIndex();
		
		// Find 8 closest lights
		for (VirtualLight light : lights) {
			float dist = light.getPosition().distanceTo(position);
			VLIndex ind = new VLIndex(light, dist);
			addIndex(ind);
		}
		
		activateIndex();
	}
	
	public static void deactivate() {
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		Light0.deactivate(gl);
		Light1.deactivate(gl);
		Light2.deactivate(gl);
		Light3.deactivate(gl);
		Light4.deactivate(gl);
		Light5.deactivate(gl);
		Light6.deactivate(gl);
		Light7.deactivate(gl);
	}
	
	private void activateIndex() {
		if (index[0] != null) index[0].light.useLight0();
		if (index[1] != null) index[1].light.useLight1();
		if (index[2] != null) index[2].light.useLight2();
		if (index[3] != null) index[3].light.useLight3();
		if (index[4] != null) index[4].light.useLight4();
		if (index[5] != null) index[5].light.useLight5();
		if (index[6] != null) index[6].light.useLight6();
		if (index[7] != null) index[7].light.useLight7();
	}
	
	private void addIndex(VLIndex i) {
		for (int j = 0; j < 8; j++) {
			if (index[j] == null) {
				index[j] = i;
				return;
			} 
			if (i.dist < index[j].dist) {
				insertIndex(i, j);
				return;
			}
		}
	}
	
	private void insertIndex(VLIndex i, int pos) {
		for (int j = 7; j > pos; j--) {
			index[j] = index[j-1];
		}
		index[pos] = i;
	}
	
	private class VLIndex {
		float dist;
		VirtualLight light;
		public VLIndex(VirtualLight light, float dist) {
			this.dist = dist;
			this.light = light;
		}
	}
	
	private void clearIndex() {
		for (int i = 0; i < 8; i++) {
			index[i] = null;
		}
	}
}
