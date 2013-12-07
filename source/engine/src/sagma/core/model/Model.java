package sagma.core.model;

import javax.media.opengl.*;

import sagma.core.data.Octree;
import sagma.core.data.VertexBuffer;
import sagma.core.material.Material;
import sagma.core.math.Vec3;
import sagma.core.model.bounding.BoundingSphere;

/**
 * MODEL
 * 
 * <p>
 * You are probably reading this model documentation because you have either:
 * </p>
 * 
 * <p><ul>
 * <li>1/ You want to create a model on the scene </li>
 * <li>2/ You are so bored you are randomly reading java documentation </li>
 * </ul></p>
 * 
 * <p>
 * Assuming the former case, the steps needed to put a Model onscreen will be detailed below.
 * It is interesting to note however, that you will probably not even touch this Model class.
 * Because when it comes to creating Models, a Model itself isn't going to do such a thing. 
 * You may or may not have noticed, but a Model is simply a mesh of data that draws on the screen
 * and has no capabilities whatsoever of loading a Model from a file.
 * </p>
 * 
 * <p>
 * The first class you should look at is ModelConstructor. It constructs Models. Moreover it can
 * also instantiate those Models for you. 
 * </p>
 * 
 * <p>
 * Now ModelConstructor gives you many ways of making a Model.
 * The main way to create a model is from a file. To do this the following code can be used:
 * <br><i>Model treeModel = ModelConstructor.makeModel(glAutoDrawable, "Models/tree.obj", scale);</i>
 * </p>
 * 
 * <p>
 * Now can I put it on the screen you ask? Of course not silly! Like stated before, a Model is
 * just a mesh of triangles. It doesn't have properties such as position, rotation, speed, gravity
 * and so on. To make a Model "come alive" you have to instantiate it. How do I instantiate a Model
 * o' knowledgeable one you ask? Who knows. I just assumed that the fairies did that for me.
 * </p>
 * 
 * <p>
 * In the off-chance that you one of these magical fairies, then you will need to know how to 
 * instantiate a Model. To do this you wave your wand and use the following code snippet:
 * <br><i>Instance treeInstance = new Instance(treeModel);</i>
 * <br>Of course, if you are a lazy magical fairy then simply prod the computer with your wand and use:
 * <br><i>Instance treeInstance = ModelConstructor.makeInstance(glAutoDrawable, "Models/tree.obj", scale);</i>
 * </p>
 * 
 * <p>
 * In the highly unlikely event that a magical fairy has produced an instance for you to place
 * in the scene. You can add the instance to the rendering engine by using:
 * <br><i>Render.add(treeInstance);</i>
 * </p>
 * 
 * 
 * @author Aaron Kison
 */
public class Model {
    private VertexBuffer buffer;
    private Vec3 location = new Vec3(0.0f, 0.0f, 0.0f);
    private Vec3 rotation = new Vec3(0.0f, 0.0f, 0.0f);
    private Vec3 scale = new Vec3(1.0f, 1.0f, 1.0f);
    private Octree tree;
    protected BoundingSphere bounds;

    /**
     * Creates a new Model using a constructed buffer.
     * 
     * Why are you trying to make a Model?
     * That's not a good thing.
     * Read the documentation for constructing a Model in the Model documentation
     * 
     * @param drawable
     * @param buffer
     */
    public Model(VertexBuffer buffer) {
        this.buffer = buffer;
        init();
        this.tree = Octree.wrap(this);
        bounds = BoundingSphere.wrap(this);
    }
    
    public Model getClone() {
    	Model m = new Model(buffer.getClone(), tree, bounds);
    	
    	m.setLocation(location);
    	m.setRotation(rotation);
    	m.setScale(scale);
    	return m;
    }
    
    public Model(VertexBuffer buffer, Octree tree) {
    	this.buffer = buffer;
    	init();
    	this.tree = tree;
    	bounds = BoundingSphere.wrap(this);
    }
    
    public Model(VertexBuffer buffer, Octree tree, BoundingSphere sphere) {
    	this.buffer = buffer;
    	init();
    	this.tree = tree;
    	bounds = sphere;
    }
    
    public Model() {

    }
    
    public Model(VertexBuffer buffer, int octreeDepth) {
        this.buffer = buffer;
        init();
        this.tree = Octree.wrap(this, octreeDepth);
        bounds = BoundingSphere.wrap(this);
    }
    
    public BoundingSphere getBounds() {
    	return bounds;
    }
    
    public Octree getOctree() {
    	return tree;
    }

    public VertexBuffer getBuffer() {
        return buffer;
    }

    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        if (buffer == null) return;
        
        gl.glPushMatrix();
        gl.glRotatef(90.0f, 1, 0, 0);
        gl.glTranslatef(location.x, location.y, location.z);
        gl.glRotatef(rotation.x, 1, 0, 0);
        gl.glRotatef(rotation.y, 0, 1, 0);
        gl.glRotatef(rotation.z, 0, 0, 1);
        gl.glScalef(scale.x, scale.y, scale.z);

        buffer.draw(gl);
        
        gl.glPopMatrix();
    }

    private void init() {
        location = new Vec3(0,0,0);
        rotation = new Vec3(0,0,0);
        scale = new Vec3(1,1,1);
    }

    public void setLocation(Vec3 v) {location = v;}
    public void setRotation(Vec3 v) {rotation = v;}
    public void setScale(Vec3 v) {scale = v; bounds.instanceGotScaled(v.x);}
    public Vec3 getLocation() {return location;}
    public Vec3 getRotation() {return rotation;}
    public Vec3 getScale() {return scale;}
    public float getMaxScale() {
    	float max = scale.x;
    	if (scale.y > max) max = scale.y;
    	if (scale.z > max) max = scale.z;
    	return max;
    }
    
    public void setMaterial(Material mat) {
    	buffer.getMesh(0).setMaterial(mat);
    }
}
