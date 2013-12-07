package sagma.core.render;

import sagma.core.math.Vec3;
import sagma.core.model.Instance;

public class Camera extends Instance {
	public Camera(Vec3 pos, Vec3 rot) {
		super();
		getState().setLocation(pos);
		getState().setRotation(rot);
		setVisible(false);
		setPickable(false);
		setSolid(false);
	}
	
	public Vec3 position() { return getState().getLocation();}
	public Vec3 rotation() { return getState().getRotation();}
	
	public float posX() {return position().x;}
	public float posY() {return position().y;}
	public float posZ() {return position().z;}
	
	public float rotX() {return rotation().x;}
	public float rotY() {return rotation().y;}
	public float rotZ() {return rotation().z;}
}
