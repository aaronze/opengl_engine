package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.material.Material;

public class Field extends BiomeType {
	public Field() {
		preferredHeight = 0.5f;
		preferredSize = 0.05f;
		ordinal = BiomeType.nextOrdinal();
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {plains,field};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return GRASS;
	}
}
