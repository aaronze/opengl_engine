package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.data.generator.field.ImageFieldGenerator;
import sagma.core.material.Material;
import sagma.core.render.Game;

public class Forest extends BiomeType {
	public Forest() {
		preferredHeight = 0.5f;;
		preferredSize = 0.1f;
		ordinal = BiomeType.nextOrdinal();
		sample = new ImageFieldGenerator(Game.getImage("Texture/Forrest.jpg"));
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {hills,forest,river,lake,village,town,field,nomad,dunes};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return GRASS;
	}
}
