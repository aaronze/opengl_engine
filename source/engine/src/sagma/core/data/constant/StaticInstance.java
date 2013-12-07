package sagma.core.data.constant;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.Octree;
import sagma.core.data.model.Collidable;
import sagma.core.data.model.Pickable;
import sagma.core.math.Vec3;
import sagma.core.model.bounding.BoundingSphere;
import sagma.core.render.Drawable;
import sagma.core.render.Steppable;

public class StaticInstance implements Pickable, Steppable, Drawable, Collidable {
	private StaticModel model;
	private Vec3 position;
	private BoundingSphere bounding;
	private Octree tree;
	private float scale = 1.0f;
	private boolean isSolid;
	
	public StaticInstance(Vec3 position, StaticModel m) {
		this.position = position;
		model = m;
		init();
	}
	
	private void init() {
		
	}
	
	@Override
	public Vec3 getLocation() {
		return new Vec3(position);
	}

	@Override
	public BoundingSphere getBounding() {
		return bounding;
	}

	@Override
	public Octree getOctree() {
		return tree;
	}

	@Override
	public float getScale() {
		return scale;
	}

	@Override
	public boolean isSolid() {
		return isSolid;
	}

	@Override
	public boolean collidesWith(Collidable i) {
		if (!boundsWith(i)) return false;
		
		if (Octree.collides(tree, getLocation(), i.getOctree(), i.getLocation()) == null) return false;
		
		return true;
	}

	@Override
	public boolean boundsWith(Collidable i) {
		return BoundingSphere.collides(this, i);
	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glPushMatrix();
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glTranslatef(position.x, position.y, position.z);
		model.draw(drawable);
		
		gl.glPopMatrix();
	}

	@Override
	public void step() {
		// Static objects don't do anything.
	}
	
	public void setSolid(boolean b) {
		isSolid = b;
	}
	
}
