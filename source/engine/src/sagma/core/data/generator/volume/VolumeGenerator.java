package sagma.core.data.generator.volume;

import sagma.core.material.Material;

public abstract class VolumeGenerator {
	public abstract Material generateNextMaterial(int depth);
	@SuppressWarnings({ "unused"})
	public Material generateNextMaterial(int depth, int time) {return null;}
	public abstract void remake();
	
	public final static int DEFAULT_DEPTH = 16;
	public final static int DEFAULT_TIME = 1;
	
	public int DEPTH = DEFAULT_DEPTH;
	public int TIME = DEFAULT_TIME;
}
