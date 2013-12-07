package sagma.core.model;


import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.media.opengl.*;

import sagma.core.data.Octree;
import sagma.core.data.generator.vector.ConstantVectorGenerator;
import sagma.core.data.manager.ModelManager;
import sagma.core.data.manager.ModelReference;
import sagma.core.data.model.Collidable;
import sagma.core.data.model.Pickable;
import sagma.core.event.CollisionEvent;
import sagma.core.math.Vec2;
import sagma.core.math.Vec3;
import sagma.core.model.bounding.BoundingSphere;
import sagma.core.network.NetObject;
import sagma.core.render.Drawable;
import sagma.core.render.Render;
import sagma.core.render.Steppable;
import sagma.games.rts.RTS;

public class Instance extends NetObject implements Drawable, Steppable, Collidable, Pickable {
	public ArrayList<Instance> children = new ArrayList<Instance>();
    protected Model model;
    private ModelBatch batch;
    protected State state;
    public Vec3 scale;
    public Instance owner;
    private boolean isVisible = true;
    private boolean isPickable = true;
    private boolean isSolid = true;
    private int id;  
    private String info;
    public String networkName = System.getProperty("user.name");
    
    private boolean pickable1 = true;
    private boolean pickable2 = false;
    private boolean pickable3 = false;
    
    
    public void setID(int i) {
        id = i;
    }
    
    public void setOwner(Instance i) {
    	owner = i;
    }
    
    public Instance getOwner() {
    	return owner;
    }

    public int getID() {
        return id;
    }

    private void init() {
	    state = new State(ConstantVectorGenerator.ZERO, ConstantVectorGenerator.ZERO);
        isVisible = true;
        scale = new Vec3(1, 1, 1);
    }
    
    public Instance(Model m) {
        init();
        model = m;
    }
    
    public Instance(String modelName) {
    	init();
    	Model m = ModelManager.get(modelName);
    	
    	if (m != null) {
    		model = m;
    	} else {
    		ModelManager.loadLater(modelName);
    		ModelManager.addReference(new ModelReference(modelName, this));
    	}
    }
    
    public Instance(Constructable c) {
    	this(c.getModelConstructor().getModel());
    }
    
    public void add(Instance i) {
    	if (children == null)
    		children = new ArrayList<Instance>();
    	children.add(i);
    }
    
    public Instance() {
    	init();
    }
    
    public State getState() {
    	return state;
    }
    
    public Instance(ModelBatch m) {
        init();
        batch = m;
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
    	pickable1 = b;
    	pickable2 = b;
    	pickable3 = b;
    }
    
    @Deprecated
    public boolean isPickable() {
    	return isPickable;
    }
    
    /**
     * @param mouseButton - expects awt.MouseEvent constant
     * @return
     */
    public boolean isPickable(int mouseButton){
    	if(mouseButton == MouseEvent.BUTTON1){
    		return pickable1;
    	}
    	else if(mouseButton == MouseEvent.BUTTON2){
    		return pickable2;
    	}
    	else if(mouseButton == MouseEvent.BUTTON3){
    		return pickable3;
    	}
    	return false;
    }
    
    /**
     * 
     * @param mouseButton - expects awt.MouseEvent constant
     * @param b
     */
    public void setPickable(int mouseButton, boolean b){
    	if(mouseButton == MouseEvent.BUTTON1){
    		pickable1 = b;
    	}
    	else if(mouseButton == MouseEvent.BUTTON2){
    		pickable2 = b;
    	}
    	else if(mouseButton == MouseEvent.BUTTON3){
    		pickable3 = b;
    	}
    }

    @Override
	public void draw(GLAutoDrawable drawable) {
    	if (isVisible && model != null) {
    		
    		GL2 gl = drawable.getGL().getGL2();
	
    		if (Render.renderPass == Render.RENDER_DRAW) {
    			gl.glPushMatrix();
	        	
    	        gl.glTranslatef(state.getX(), state.getY(), state.getZ());
    	        gl.glRotatef(state.getRotX(), 1, 0, 0);
    	        gl.glRotatef(state.getRotY(), 0, 1, 0);
    	        gl.glRotatef(state.getRotZ(), 0, 0, 1);
    	        
 	        	drawModel(drawable);
 	        	
 	        	gl.glPopMatrix();
 	        } else if (Render.renderPass == Render.RENDER_LIGHTING) {
		        gl.glPushMatrix();
		        Render.lightManager.activateLightsForPosition(getPosition());
		        	
		        gl.glTranslatef(state.getX(), state.getY(), state.getZ());
		        gl.glRotatef(state.getRotX(), 1, 0, 0);
		        gl.glRotatef(state.getRotY(), 0, 1, 0);
		        gl.glRotatef(state.getRotZ(), 0, 0, 1);
		        
		        drawModel(drawable);
	
		        gl.glPopMatrix();
 	        }
    	}
    }
    
    private void drawModel(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	
    	gl.glPushMatrix();
    	gl.glScalef(scale.x, scale.y, scale.z);
    	if (model != null) {
            model.draw(drawable);
        }
        if (batch != null) {
            batch.draw(drawable);
        }
        gl.glPopMatrix();
        
        if (children != null) {
			Iterator<Instance> list = children.iterator();
			while (list.hasNext()) {
				list.next().draw(drawable);
			}
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
	
	@Override
	public boolean collidesWith(Collidable i) {
		for (Instance child : children) {
			if (child.collidesWith(i)) return true;
		}
		
		if (!isSolid) return false;
		
		if (!boundsWith(i)) return false;

		CollisionEvent collision = Octree.collides(getOctree(), getLocation(), 
				i.getOctree(), i.getLocation());
		if (getOctree() != null && i.getOctree() != null && collision == null) return false;
		
		return true;
	}
	
	@Override
	public boolean boundsWith(Collidable i) {
		for (Instance child : children) {
			if (child.boundsWith(i)) return true;
		}
		
		if (!isSolid) return false;
		
		return BoundingSphere.collides(this, i);
	}
	
	public Model model() {
		return model;
	}
	
	public void setLocation(Vec3 v) {getState().setLocation(v.x, v.y, v.z);}
	public void setLocation(Vec2 v, float z) {getState().setLocation(v.x, v.y, z);}
	public void setLocation(float x, Vec2 v) {getState().setLocation(x, v.x, v.y);}
	public void setLocation(float x, float y, float z) {getState().setLocation(x, y, z);}
	
	public void setPosition(Vec3 v) {getState().setLocation(v.x, v.y, v.z);}
	public void setPosition(Vec2 v, float z) {getState().setLocation(v.x, v.y, z);}
	public void setPosition(float x, Vec2 v) {getState().setLocation(x, v.x, v.y);}
	public void setPosition(float x, float y, float z) {getState().setLocation(x, y, z);}
	
	public void addLocation(Vec3 v) {getState().addLocation(v.x, v.y, v.z);}
	public void addLocation(Vec2 v, float z) {getState().addLocation(v.x, v.y, z);}
	public void addLocation(float x, Vec2 v) {getState().addLocation(x, v.x, v.y);}
	public void addLocation(float x, float y, float z) {getState().addLocation(x, y, z);}
	
	public void addPosition(Vec3 v) {getState().addLocation(v.x, v.y, v.z);}
	public void addPosition(Vec2 v, float z) {getState().addLocation(v.x, v.y, z);}
	public void addPosition(float x, Vec2 v) {getState().addLocation(x, v.x, v.y);}
	public void addPosition(float x, float y, float z) {getState().addLocation(x, y, z);}
	
	public void setSpeed(Vec3 v) {getState().setSpeed(v.x, v.y, v.z);}
	public void setSpeed(Vec2 v, float z) {getState().setSpeed(v.x, v.y, z);}
	public void setSpeed(float x, Vec2 v) {getState().setSpeed(x, v.x, v.y);}
	public void setSpeed(float x, float y, float z) {getState().setSpeed(x, y, z);}
	
	public void setVelocity(Vec3 v) {getState().setSpeed(v.x, v.y, v.z);}
	public void setVelocity(Vec2 v, float z) {getState().setSpeed(v.x, v.y, z);}
	public void setVelocity(float x, Vec2 v) {getState().setSpeed(x, v.x, v.y);}
	public void setVelocity(float x, float y, float z) {getState().setSpeed(x, y, z);}
	
	public void addAcceleration(Vec3 v) {getState().addAcceleration(v);}
	public void addAcceleration(Vec2 v, float z) {getState().addAcceleration(new Vec3(v, z));}
	public void addAcceleration(float x, Vec2 v) {getState().addAcceleration(new Vec3(x, v));}
	public void addAcceleration(float x, float y, float z) {getState().addAcceleration(x, y, z);}
	
	public void setAcceleration(Vec3 v) {getState().setAcceleration(v);}
	public void setAcceleration(Vec2 v, float z) {getState().setAcceleration(new Vec3(v, z));}
	public void setAcceleration(float x, Vec2 v) {getState().setAcceleration(new Vec3(x, v));}
	public void setAcceleration(float x, float y, float z) {getState().setAcceleration(x, y, z);}
	
	@Override
	public Vec3 getLocation() {return getState().getLocation();}
	public Vec3 getPosition() {return getState().getLocation();}
	public Vec3 getSpeed() {return getState().getSpeed();}
	public Vec3 getVelocity() {return getState().getSpeed();}
	
	public Vec3 getAcceleration() {return getState().getAcceleration();}
	public Vec3 getRotation() {return getState().getRotation();}
	public void setRotation(Vec3 v) {getState().setRotation(v);}
	public void setDirection(Vec3 v) {getState().setRotation(v);}
	
	public void setRotation(float x, float y, float z) {getState().setRotation(x, y, z);}
	public void setRotation(Vec2 xy, float z) {getState().setRotation(xy.x, xy.y, z);}
	public void setRotation(float x, Vec3 yz) {getState().getRotation().set(x, yz.x, yz.y);}
	
	public void setDirection(float x, float y, float z) {getState().setRotation(x, y, z);}
	public void addRotation(Vec3 v) {getState().addRotation(v.x, v.y, v.z);}
	public void addDirection(Vec3 v) {getState().addRotation(v.x, v.y, v.z);}
	public void addRotation(float x, float y, float z) {getState().addRotation(x, y, z);}
	public void addDirection(float x, float y, float z) {getState().addRotation(x, y, z);}
	
	public void addSpeed(Vec3 v) {getState().addSpeed(v);}
	public void addVelocity(Vec3 v) {getState().addSpeed(v);}
	public void addSpeed(float x, float y, float z) {getState().addSpeed(x, y, z);}
	
	public void addVelocity(float x, float y, float z) {getState().addSpeed(x, y, z);}
	public void addRotationalSpeed(float x, float y, float z) {getState().getRotationalSpeed().m_add(x, y, z);}
	public Vec3 getRotationalSpeed() {return getState().getRotationalSpeed();}
	
	public void setModel(Model m) {
		model = m;
	}

	@Override
	public BoundingSphere getBounding() {
		if (model == null) return null;
		return model.getBounds();
	}

	@Override
	public Octree getOctree() {
		if (model == null) return null;
		return model.getOctree();
	}
	
	public String getInfo() {
		if (info == null) {
			info = ""+RTS.client.id + this.toString().substring(this.toString().lastIndexOf('@'));
		}
		return info;
	}
	
}