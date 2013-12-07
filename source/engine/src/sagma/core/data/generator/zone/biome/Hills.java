package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.material.Material;

public class Hills extends BiomeType {
	public Hills() {
		preferredHeight = 1.4f;
		preferredSize = 0.1f;
		ordinal = BiomeType.nextOrdinal();
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {plains,hills,forest,village,field,nomad,dunes,mountains};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return GRASS;
	}
}
