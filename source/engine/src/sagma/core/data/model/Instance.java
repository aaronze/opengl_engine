package sagma.core.data.model;

import java.util.ArrayList;
import java.util.Iterator;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.fixedfunc.GLPointerFunc;

import sagma.core.data.Octree;
import sagma.core.data.generator.vector.ConstantVectorGenerator;
import sagma.core.math.Vec2;
import sagma.core.math.Vec3;
import sagma.core.data.model.Model;
import sagma.core.event.CollisionEvent;
import sagma.core.model.State;
import sagma.core.model.bounding.BoundingSphere;
import sagma.core.profile.Profiler;
import sagma.core.render.Drawable;
import sagma.core.render.Steppable;

public class Instance implements Drawable, Steppable, Collidable, Pickable {
	private ArrayList<Instance> children;
    protected Model model;
    private State state;
    private Vec3 scale;
    private boolean isVisible = true;
    private boolean isSolid = true;
    private boolean isPickable = true;
    
    public Instance(Model m) {
        init();
        model = m;
    }
    
    private void init() {
	    state = new State(new ConstantVectorGenerator(new Vec3(0, 0, 0)), 
	    				  new ConstantVectorGenerator(new Vec3(0, 0, 0)));
        isVisible = true;
        scale = new Vec3(1, 1, 1);
    }
    
    public void add(Instance i) {
    	if (children == null)
    		children = new ArrayList<Instance>();
    	children.add(i);
    }
    
    public void setVisible(boolean b) {
        isVisible = b;
    }

    public boolean isVisible() {
        return isVisible;
    }
    
    public void setSolid(boolean b) {
    	isSolid = b;
    }
    
    @Override
	public boolean isSolid() {
    	return isSolid;
    }
    
    public void setPickable(boolean b) {
    	isPickable = b;
    }
    
    public boolean isPickable() {
    	return isPickable;
    }

    @Override
	public void draw(GLAutoDrawable drawable) {
    	if (isVisible) {
    		Profiler.start("Time spent drawing using New Engine");
	        GL2 gl = drawable.getGL().getGL2();
	        
	        gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
	        gl.glEnableClientState(GLPointerFunc.GL_NORMAL_ARRAY);
	        gl.glEnableClientState(GLPointerFunc.GL_TEXTURE_COORD_ARRAY);
	
	        gl.glPushMatrix();
	
	        gl.glTranslatef(state.getX(), state.getY(), state.getZ());
	        gl.glRotatef(state.getRotX(), 1, 0, 0);
	        gl.glRotatef(state.getRotY(), 0, 1, 0);
	        gl.glRotatef(state.getRotZ(), 0, 0, 1);
	        gl.glScalef(scale.x, scale.y, scale.z);
	
	        if (model != null) {
	            model.draw(gl);
	        }
	        
	        if (children != null) {
    			Iterator<Instance> list = children.iterator();
    			while (list.hasNext()) {
    				list.next().draw(drawable);
    			}
    		}
	
	        gl.glPopMatrix();
	        
	        gl.glDisableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
	        gl.glDisableClientState(GLPointerFunc.GL_NORMAL_ARRAY);
	        gl.glDisableClientState(GLPointerFunc.GL_TEXTURE_COORD_ARRAY);
	        
	        Profiler.stop("Time spent drawing using New Engine");
    	}
    }

	@Override
	public void step() {
		state.step();
		heartbeat();
		
		if (children != null) {
			Iterator<Instance> list = children.iterator();
			while (list.hasNext()) {
				list.next().step();
			}
		}
	}
	
	public void heartbeat() {
		
	}

	public void setScale(float d) {
		scale = new Vec3(d, d, d);
	}
	
	public void setSize(float d) {
		scale = new Vec3(d, d, d);
	}
	
	@Override
	public float getScale() {
		return scale.x;
	}
	
	public void setLocation(Vec3 v) {state.setLocation(v.x, v.y, v.z);}
	public void setLocation(Vec2 v, float z) {state.setLocation(v.x, v.y, z);}
	public void setLocation(float x, Vec2 v) {state.setLocation(x, v.x, v.y);}
	public void setLocation(float x, float y, float z) {state.setLocation(x, y, z);}
	
	public void setPosition(Vec3 v) {state.setLocation(v.x, v.y, v.z);}
	public void setPosition(Vec2 v, float z) {state.setLocation(v.x, v.y, z);}
	public void setPosition(float x, Vec2 v) {state.setLocation(x, v.x, v.y);}
	public void setPosition(float x, float y, float z) {state.setLocation(x, y, z);}
	
	public void addLocation(Vec3 v) {state.addLocation(v.x, v.y, v.z);}
	public void addLocation(Vec2 v, float z) {state.addLocation(v.x, v.y, z);}
	public void addLocation(float x, Vec2 v) {state.addLocation(x, v.x, v.y);}
	public void addLocation(float x, float y, float z) {state.addLocation(x, y, z);}
	
	public void addPosition(Vec3 v) {state.addLocation(v.x, v.y, v.z);}
	public void addPosition(Vec2 v, float z) {state.addLocation(v.x, v.y, z);}
	public void addPosition(float x, Vec2 v) {state.addLocation(x, v.x, v.y);}
	public void addPosition(float x, float y, float z) {state.addLocation(x, y, z);}
	
	public void setSpeed(Vec3 v) {state.setSpeed(v.x, v.y, v.z);}
	public void setSpeed(Vec2 v, float z) {state.setSpeed(v.x, v.y, z);}
	public void setSpeed(float x, Vec2 v) {state.setSpeed(x, v.x, v.y);}
	public void setSpeed(float x, float y, float z) {state.setSpeed(x, y, z);}
	
	public void setVelocity(Vec3 v) {state.setSpeed(v.x, v.y, v.z);}
	public void setVelocity(Vec2 v, float z) {state.setSpeed(v.x, v.y, z);}
	public void setVelocity(float x, Vec2 v) {state.setSpeed(x, v.x, v.y);}
	public void setVelocity(float x, float y, float z) {state.setSpeed(x, y, z);}
	
	public void addAcceleration(Vec3 v) {state.addAcceleration(v);}
	public void addAcceleration(Vec2 v, float z) {state.addAcceleration(new Vec3(v, z));}
	public void addAcceleration(float x, Vec2 v) {state.addAcceleration(new Vec3(x, v));}
	public void addAcceleration(float x, float y, float z) {state.addAcceleration(x, y, z);}
	
	public void setAcceleration(Vec3 v) {state.setAcceleration(v);}
	public void setAcceleration(Vec2 v, float z) {state.setAcceleration(new Vec3(v, z));}
	public void setAcceleration(float x, Vec2 v) {state.setAcceleration(new Vec3(x, v));}
	public void setAcceleration(float x, float y, float z) {state.setAcceleration(x, y, z);}
	
	@Override
	public Vec3 getLocation() {
		return state.getLocation();
	}
	
	public Vec3 getPosition() {
		return state.getLocation();
	}
	
	public Vec3 getSpeed() {
		return state.getSpeed();
	}
	
	public Vec3 getVelocity() {
		return state.getSpeed();
	}
	
	public Vec3 getAcceleration() {
		return state.getAcceleration();
	}
	
	public Vec3 getRotation() {
		return state.getRotation();
	}
	
	public void setRotation(Vec3 v) {
		state.setRotation(v);
	}
	
	public void setDirection(Vec3 v) {
		state.setRotation(v);
	}
	
	public void setRotation(float x, float y, float z) {
		state.setRotation(new Vec3(x, y, z));
	}
	
	public void setDirection(float x, float y, float z) {
		state.setRotation(new Vec3(x, y, z));
	}
	
	public void addRotation(Vec3 v) {
		state.addRotation(v);
	}
	
	public void addDirection(Vec3 v) {
		state.addRotation(v);
	}
	
	public void addRotation(float x, float y, float z) {
		state.getRotation().m_add(x, y, z);
	}
	
	public void addDirection(float x, float y, float z) {
		state.getRotation().m_add(x, y, z);
	}
	
	public void addSpeed(Vec3 v) {
		state.addSpeed(v);
	}
	
	public void addVelocity(Vec3 v) {
		state.addSpeed(v);
	}
	
	public void addSpeed(float x, float y, float z) {
		state.getSpeed().m_add(x, y, z);
	}
	
	public void addVelocity(float x, float y, float z) {
		state.getSpeed().m_add(x, y, z);
	}
	
	public void addRotationalSpeed(float x, float y, float z) {
		state.addRotationalSpeed(new Vec3(x, y, z));
	}
	
	@Override
	public boolean collidesWith(Collidable i) {
		if (!isSolid) return false;
		
		if (!boundsWith(i)) return false;
		
		CollisionEvent collision = Octree.collides(getOctree(), getLocation(), 
				i.getOctree(), i.getLocation());
		if (collision == null) return false;
		
		return true;
	}
	
	@Override
	public boolean boundsWith(Collidable i) {
		boolean b = BoundingSphere.collides(this, i);
		
		return b;
	}

	@Override
	public BoundingSphere getBounding() {
		return model.getBounds();
	}

	@Override
	public Octree getOctree() {
		return model.getOctree();
	}
}
