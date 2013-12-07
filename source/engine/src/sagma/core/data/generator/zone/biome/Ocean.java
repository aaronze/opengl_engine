package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.data.generator.field.ImageFieldGenerator;
import sagma.core.material.Material;
import sagma.core.render.Game;

public class Ocean extends BiomeType {
	public Ocean() {
		preferredHeight = -0.2f;
		preferredSize = 0.4f;
		ordinal = BiomeType.nextOrdinal();
		sample = new ImageFieldGenerator(Game.getImage("Texture/Water.jpg"));
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {ocean,beach,dunes};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return WATER;
	}
}
