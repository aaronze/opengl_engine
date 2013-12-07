package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;

public class Village extends BiomeType {
	public Village() {
		preferredHeight = 1.0f;
		preferredSize = 0.02f;
		ordinal = BiomeType.nextOrdinal();
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {plains,hills,forest,desert,snow,river,lake,field,beach,dunes};
	}
}
