package sagma.core.material;

import java.awt.image.BufferedImage;
import javax.media.opengl.GL2;

import sagma.core.data.Extradicator;
import sagma.core.model.Model;
import sagma.core.render.Render;

/**
 * Not ready for use yet
 * 
 * @author Aaron Kison
 *
 */
@Deprecated
public class Extradication extends Material {
	private Model owner;
	private Texture currentTexture;
	private double[] pops;
	private int maxDepth;
	private int currentDepth;
	private Extradicator extradicator;
	private BufferedImage image;
	
	public Extradication(String filename) {		
		extradicator = new Extradicator(filename);
		image = new BufferedImage(extradicator.getWidth(), extradicator.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		maxDepth = extradicator.getMaxDepth();
		currentDepth = 1;
		
		init();
	}
	
	private void init() {
		pops = new double[maxDepth];
		
		for (int i = 0; i < maxDepth; i++) {
			pops[i] = 1000.0*i*i;
		}
	}
	
	@Override
	public void activate(GL2 gl) {
		if (owner != null) {
			double distance = Render.camera.position().subtract(owner.getLocation()).length();
			System.out.println(" " + distance);
			int i;
			for (i = 0; i < maxDepth && distance > pops[i]; i++);
			i = maxDepth-i-1;
			if (i != currentDepth) {
				currentDepth = i;
				pop(gl);
			}
			currentTexture.activate(gl);
		}
	}
	
	public void setOwner(Model m) {
		owner = m;
	}
	
	private void pop(GL2 gl) {
		System.out.println("POP! " + currentDepth);
		
		extradicator.draw(image.getGraphics(), currentDepth);
		currentTexture = new Texture(gl, image);
	}

	@Override
	public void deactivate(GL2 gl) {
		if (owner != null) {
			currentTexture.deactivate(gl);
		}
	}
	
}
