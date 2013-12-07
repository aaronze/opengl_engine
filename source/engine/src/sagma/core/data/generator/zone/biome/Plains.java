package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.data.generator.field.ImageFieldGenerator;
import sagma.core.material.Material;
import sagma.core.render.Game;

public class Plains extends BiomeType {
	public Plains() {
		preferredHeight = 1.0f;
		preferredSize = 0.1f;
		ordinal = BiomeType.nextOrdinal();
		sample = new ImageFieldGenerator(Game.getImage("Texture/earth-living.jpg"));
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {crater,plains,hills,desert,snow,river,lake,village,town,field,beach,mountains};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return GRASS;
	}
}
