package sagma.core.data.constant;

import java.awt.image.BufferedImage;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.point.PointCloud;
import sagma.core.math.ModelDimension;
import sagma.core.model.Model;
import sagma.core.model.ModelConstructor;
import sagma.core.render.Game;

public class StaticModel {
	public final static int POINTS_PER_TEX = 512;
	private PointCloud pointCloud;
	
	public StaticModel(String filename) {
		Model model = ModelConstructor.makeModel(Game.savedDrawable, filename, 1);
		
		pointCloud = new PointCloud();
		pointCloud.wrap(model.getBuffer().getMesh(0).iterator());
		
		init();
	}
	
	public StaticModel(PointCloud cloud) {
		pointCloud = cloud;
		
		init();
	}
	
	/**
	 * TODO: Write into a int[] and then copy directly.
	 * 
	 * This method takes 6 camera viewport snapshots of each of the 6 sides of the
	 * poing cloud object and then uses these snapshots as textures on a cube.
	 * 
	 */
	@SuppressWarnings("unused")
	private void init() {
		ModelDimension dimensions = pointCloud.getDimensions();
		int width = (int)((dimensions.maxX - dimensions.minX) * POINTS_PER_TEX);
		int height = (int)((dimensions.maxY - dimensions.minY) * POINTS_PER_TEX);
		int depth = (int)((dimensions.maxZ - dimensions.minZ) * POINTS_PER_TEX); 
		
		BufferedImage FrontPlate = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		BufferedImage BackPlate = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for (float x = dimensions.minX; x <= dimensions.maxX; x += 1.0f/pointCloud.POINT_DENSITY) {
			for (float y = dimensions.minY; y <= dimensions.maxY; y += 1.0f/pointCloud.POINT_DENSITY) {
				
			}
		}
	}

	public void draw(GLAutoDrawable drawable) {
		pointCloud.draw(drawable);
	}
}
