package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.data.generator.field.ImageFieldGenerator;
import sagma.core.material.Material;
import sagma.core.render.Game;

public class Desert extends BiomeType {
	public Desert() {
		preferredHeight = 0.2f;
		preferredSize = 0.3f;
		ordinal = BiomeType.nextOrdinal();
		sample = new ImageFieldGenerator(Game.getImage("Texture/Desert.jpg"));
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {plains,desert,village,lava,dunes,crater};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return SAND;
	}
}
