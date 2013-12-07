package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;

public class Town extends BiomeType {
	public Town() {
		preferredHeight = 1.0f;
		preferredSize = 0.05f;
		ordinal = BiomeType.nextOrdinal();
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {plains,forest,river,field,beach};
	}
}
