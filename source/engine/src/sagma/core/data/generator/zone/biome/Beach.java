package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.data.generator.field.ImageFieldGenerator;
import sagma.core.material.Material;
import sagma.core.render.Game;

public class Beach extends BiomeType {
	public Beach() {
		preferredHeight = 0.1f;
		preferredSize = 0.2f;
		ordinal = BiomeType.nextOrdinal();
		sample = new ImageFieldGenerator(Game.getImage("Texture/moon.jpg"));
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {dunes,ocean,plains};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return SAND;
	}
}
