package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.material.Material;

public class Mountains extends BiomeType {
	public Mountains() {
		preferredHeight = 2.3f;
		preferredSize = 0.1f;
		ordinal = BiomeType.nextOrdinal();
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {plains,hills,desert,snow};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return SNOW;
	}
}
