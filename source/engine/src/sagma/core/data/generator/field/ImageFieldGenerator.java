package sagma.core.data.generator.field;

import java.awt.image.BufferedImage;

import sagma.core.math.Vec3;

public class ImageFieldGenerator extends FieldGenerator {
	private int width;
	private int height;
	private BufferedImage image;
	
	public ImageFieldGenerator(BufferedImage img) {
		image = img;
		width = image.getWidth();
		height = image.getHeight();
	}
	
	public int getColorAtPoint(float u, float v) {
		int x = (int)(u * width);
		int y = (int)(v * height);
		while (x < 0) x += width;
		while (x >= width) x-= width;
		while (y < 0) y += height;
		while (y >= height) y -= height;
		return image.getRGB(x, y);
	}
	
	@Override
	public float nextField(Vec3 field) {
		Vec3 uv = getUVOfVector(field);

		float val = valueAtTexturePoint(uv.x, uv.y);
		return val;
		//return field.z;
	}
	
	public static Vec3 getUVOfVector(Vec3 vec) {
		vec.m_normalize();
		float x = vec.x;
		float y = vec.z;
		
		float PI = (float)Math.PI;
		
		float v = (float)Math.acos(y) / PI;
		float u = 0;
		if (vec.y < 0) u = 1.0f - u;
		
		if (vec.y == 0) {
			if (vec.x < 0) u = 0.5f;
		} else {
			u = (float)Math.acos(x/Math.sin(PI*v)) / (2 * PI);
		}
		
		return new Vec3(u, v, 0);
	}
	
	public float valueAtTexturePoint(float u, float v) {
		int x = (int)(u * width);
		int y = (int)(v * height);
		if (x >= 0 && x < width && y >= 0 && y < height) 
			return image.getRGB(x, y) & 0xFF;
		return 0;
	}
	
	public float valueAtTexture(int x, int y) {
		return image.getRGB(x, y) & 0xFF;
	}

}
