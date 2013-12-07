package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.material.Material;

public class Crater extends BiomeType {
	public Crater() {
		preferredHeight = -3.1f;
		preferredSize = 0.1f;
		ordinal = BiomeType.nextOrdinal();
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {desert,plains,dunes};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return DIRT;
	}
}