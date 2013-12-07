package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.data.generator.field.ImageFieldGenerator;
import sagma.core.material.Material;
import sagma.core.render.Game;

public class Snow extends BiomeType {
	public Snow() {
		preferredHeight = 0.3f;
		preferredSize = 0.1f;
		ordinal = BiomeType.nextOrdinal();
		sample = new ImageFieldGenerator(Game.getImage("Texture/Ice.jpg"));
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {plains,snow,ice,river,lake,village,beach,mountains};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return SNOW;
	}
}
