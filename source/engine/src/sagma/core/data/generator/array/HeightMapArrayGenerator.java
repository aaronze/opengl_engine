package sagma.core.data.generator.array;

import java.awt.image.BufferedImage;

/**
 * Generates arrays using height-map information from a given height-map image
 * 
 * @author Aaron Kison
 *
 */
public class HeightMapArrayGenerator extends ArrayGenerator {
	float[][] array;
	
	public HeightMapArrayGenerator(BufferedImage map, float scale) {
		int mapWidth = map.getWidth();
		int mapHeight = map.getHeight();
		array = new float[mapWidth][mapHeight];
		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				int r = map.getRGB(x, y) & 0xFF;
				float val = r * scale / 255;
				array[x][y] = val;
			}
		}
	}

	@Override
	public float[] nextArray1D() {
		return null;
	}

	@Override
	public float[][] nextArray2D() {
		return array;
	}

	@Override
	public float[][][] nextArray3D() {
		return null;
	}
	
	
}
