package sagma.core.data.model;

import sagma.core.data.Octree;
import sagma.core.math.Vec3;
import sagma.core.model.bounding.BoundingSphere;

public interface Collidable {
	public BoundingSphere getBounding();
	public Octree getOctree();
	public Vec3 getLocation();
	public float getScale();
	public boolean isSolid();
	public boolean collidesWith(Collidable i);
	public boolean boundsWith(Collidable i);
}
