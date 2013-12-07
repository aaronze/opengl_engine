package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.material.Material;
public class Ice extends BiomeType {
	public Ice() {
		preferredHeight = 0.0f;
		preferredSize = 0.05f;
		ordinal = BiomeType.nextOrdinal();
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {snow,ice};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return SNOW;
	}
}
