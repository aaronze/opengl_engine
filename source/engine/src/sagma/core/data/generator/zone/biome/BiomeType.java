package sagma.core.data.generator.zone.biome;

import java.awt.Color;

import sagma.core.data.generator.field.ImageFieldGenerator;
import sagma.core.material.Material;
import sagma.core.material.Shader;

public abstract class BiomeType {
	public final static Shader GRASS = new Shader("Shaders/Biome/grass");
	public final static Shader SNOW = new Shader("Shaders/Biome/snow");
	public final static Shader DIRT = new Shader("Shaders/Biome/dirt");
	public final static Shader SAND = new Shader("Shaders/Biome/sand");
	public final static Shader WATER = new Shader("Shaders/Biome/water");
	public final static Shader LAVA = new Shader("Shaders/Biome/lava");
	public final static Shader DEFAULT = new Shader("Shaders/gooch");
	
	protected float preferredSize = 1.0f;
	protected float preferredHeight = 1.0f;
	protected int ordinal;
	protected BiomeType[] relatedBiomes;
	protected ImageFieldGenerator sample;
	
	public float getPreferredSize() {return preferredSize;}
	public float getPreferredHeight() {return preferredHeight;}
	public BiomeType[] getRelatedBiomes() {return relatedBiomes;}
	public int ordinal() {return ordinal;}
	@SuppressWarnings({"unused" })
	public Material getMaterialForHeight(float height) {
		return DEFAULT;
	}
	public abstract void buildDependencies();
	public float getHeightAtLocation(float u, float v) {
		if (sample == null) return getPreferredHeight();
		return sample.valueAtTexturePoint(u, v) * 0.01f;
	}
	public int getRGBAtLocation(float u, float v) {
		if (sample == null) return Color.BLACK.getRGB();
		return sample.getColorAtPoint(u, v);
	}
	
	private static int ordinalCounter = 0;
	
	public static final int nextOrdinal() {
		return ordinalCounter++;
	}
	
}
