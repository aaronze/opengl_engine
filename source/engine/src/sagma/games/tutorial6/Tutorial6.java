package sagma.games.tutorial6;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.generator.planetoid.DirectedFieldPlanetoidGenerator;
import sagma.core.data.generator.planetoid.PlanetoidGenerator;
import sagma.core.render.Game;

public class Tutorial6 extends Game {
	@Override
	public void init(GLAutoDrawable drawable) {
		setEscapeKeyToExit();
		setGameMode(GAMEMODE_3D);
		
		addDefaultKeyBindings();
		
		setCameraLocation(0, 0, 3.5f);
		setCameraRotation(0, -180, 0);
		
		PlanetoidGenerator planetMaker = new DirectedFieldPlanetoidGenerator("name",
				"Texture/world_heightmap.jpg", "Texture/earth-living.jpg");
		make(planetMaker);
		
	}
}
