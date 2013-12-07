package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.material.Material;

public class Lake extends BiomeType {
	public Lake() {
		preferredHeight = 0.0f;
		preferredSize = 0.05f;
		ordinal = BiomeType.nextOrdinal();
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {plains,forest,snow,river,lake,village,field};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return WATER;
	}
}
