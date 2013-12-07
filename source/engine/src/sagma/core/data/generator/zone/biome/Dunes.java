package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.material.Material;

public class Dunes extends BiomeType {
	public Dunes() {
		preferredHeight = 0.4f;
		preferredSize = 0.02f;
		ordinal = BiomeType.nextOrdinal();
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {crater,plains,dunes,desert,beach};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return SAND;
	}
}
