package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.material.Material;

public class River extends BiomeType {
	public River() {
		preferredHeight = -0.1f;
		preferredSize = 0.2f;
		ordinal = BiomeType.nextOrdinal();
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {plains,hills,forest,village,town,city,ocean,field,beach,dunes};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return WATER;
	}
}
