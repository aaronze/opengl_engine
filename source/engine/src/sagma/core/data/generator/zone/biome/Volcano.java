package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.material.Material;

public class Volcano extends BiomeType {
	public Volcano() {
		preferredHeight = 2.8f;
		preferredSize = 0.2f;
		ordinal = BiomeType.nextOrdinal();
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {lava,lava,lava};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return LAVA;
	}
}
