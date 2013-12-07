package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.material.Material;

public class Nomad extends BiomeType {
	public Nomad() {
		preferredHeight = 1.0f;
		preferredSize = 0.01f;
		ordinal = BiomeType.nextOrdinal();
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {plains,hills,desert,snow,river,lake,village,town,field,beach};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return DIRT;
	}
}
