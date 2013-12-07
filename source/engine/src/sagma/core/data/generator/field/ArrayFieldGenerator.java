package sagma.core.data.generator.field;

import sagma.core.math.Vec3;

public class ArrayFieldGenerator extends FieldGenerator {
	private int width;
	private int height;
	private float[][] image;
	
	public ArrayFieldGenerator(float[][] img) {
		image = img;
		width = image.length;
		height = image[0].length;
	}
	
	@Override
	public float nextField(Vec3 field) {
		Vec3 uv = getUVOfVector(field);

		float val = valueAtTexturePoint(uv.x, uv.y);
		return val;
	}
	
	public static Vec3 getUVOfVector(Vec3 vec) {
		return ImageFieldGenerator.getUVOfVector(vec);
	}
	
	private float valueAtTexturePoint(float u, float v) {
		int x = (int)(u * width);
		int y = (int)(v * height);
		if (x >= 0 && x < width && y >= 0 && y < height) 
			return image[x][y];
		return 0;
	}
}
