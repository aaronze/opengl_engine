package sagma.core.data.generator.zone;

import java.util.ArrayList;

import sagma.core.data.generator.zone.biome.BiomeType;

public class Biome {
	public final static int BIOME_TYPES = 18;
	private ArrayList<Biome>relatedBiomes;
	private BiomeType type;
	
	public Biome(BiomeType type) {
		this.type = type;
		relatedBiomes = new ArrayList<Biome>(16);
	}
	
	public void addRelatedBiome(Biome b) {
		relatedBiomes.add(b);
	}
	
	public void addRelatedBiomes(Biome[] b) {
		for (int i = 0; i < b.length; i++) addRelatedBiome(b[i]);
	}
	
	public ArrayList<Biome> getRelatedBiomes() {
		return relatedBiomes;
	}
	
	public BiomeType getType() {
		return type;
	}
}
