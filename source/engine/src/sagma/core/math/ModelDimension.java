package sagma.core.math;

public class ModelDimension {
	public float minX, minY, minZ;
	public float maxX, maxY, maxZ;
	
	public ModelDimension(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
}
