package sagma.core.model;


import javax.media.opengl.*;

import sagma.core.data.VertexBuffer;
import sagma.core.data.generator.volume.CloudVolumeGenerator;
import sagma.core.data.generator.volume.VolumeGenerator;
import sagma.core.material.Material;
import sagma.core.math.Vec3;

public class VolumeModel extends Model {
	private float SCALE = 1.005f;
    private VolumeGenerator volumeGen;
    private int depth = 8;
    private int time = 8;
    private Material[][] materials;
    private Material def;
    private float t = 0;
    private float tStep = 0.25f;
    private float u = 0;
    private float uStep = 0.25f;
    
    /**
     * Deprecated due to restricted sets. Only creates the default cloud volume.
     * User VolumeModel(VertexBuffer, VolumeGenerator) instead.
     * 
     * @param model
     */
    @Deprecated
    public VolumeModel(Model model) {
    	super(model.getBuffer());
    	volumeGen = new CloudVolumeGenerator(depth, time);
    	materials = new Material[time][depth];
    	
    	init();
    }
    
    public VolumeModel(Constructable c, VolumeGenerator vG) {
    	this(c.getModelConstructor().getModel().getBuffer(), vG);
    }

    public VolumeModel(VertexBuffer buffer, VolumeGenerator vG) {
        super(buffer);
        volumeGen = vG;
        depth = vG.DEPTH;
        time = vG.TIME;
        materials = new Material[time][depth];

        init();
    }

    private void init() {
    	def = Material.getMaterial("Texture/earth-living.jpg");
    	setRotation(new Vec3(90, 0, 0));
    	
    	for (int j = 0; j < time; j++) {
	        for (int k = 0; k < depth; k++) {
	            materials[j][k] = volumeGen.generateNextMaterial(k, j);
	        }
    	}
    }

    @Override
	public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        
        gl.glDisable(GL.GL_BLEND);
        VertexBuffer buffer = getBuffer();
        buffer.setMaterial(def);
        buffer.draw(gl);
        
        gl.glPushMatrix();
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);

        float scale = SCALE;
        for (int k = 0; k < depth; k++) {
        	gl.glScalef(scale, scale, scale);
            buffer.setMaterial(materials[0][k]);
            buffer.draw(gl);
        }
       
        if (t+tStep >= depth || t+tStep < 0) {
        	tStep = -tStep;
        	
        	if (u+uStep >= time || u+uStep < 0) {
        		uStep = -uStep;
        	}
        	u += uStep;
        }
        t += tStep;

        //gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDisable(GL.GL_BLEND);
        gl.glPopMatrix();
    }

    public int getDepth() {
        return depth;
    }
}