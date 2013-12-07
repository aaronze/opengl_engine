package sagma.core.data.generator.zone.biome;

import static sagma.core.data.generator.zone.BiomeManager.*;
import sagma.core.data.generator.field.ImageFieldGenerator;
import sagma.core.material.Material;
import sagma.core.render.Game;

public class Lava extends BiomeType {
	public Lava() {
		preferredHeight = -0.2f;
		preferredSize = 0.05f;
		ordinal = BiomeType.nextOrdinal();
		sample = new ImageFieldGenerator(Game.getImage("Texture/Volcanic.jpg"));
	}
	
	@Override
	public void buildDependencies() {
		relatedBiomes = new BiomeType[] {lava,lava,lava,lava,lava,volcano};
	}
	
	@Override
	public Material getMaterialForHeight(float height) {
		return LAVA;
	}
}
