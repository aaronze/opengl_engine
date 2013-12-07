package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;

public class City extends BiomeType {
	public City() {
		preferredHeight = 1.0f;
		preferredSize = 0.3f;
		ordinal = BiomeType.nextOrdinal();
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {field};
	}
}
