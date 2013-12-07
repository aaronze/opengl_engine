package sagma.games.fps;

import java.io.IOException;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.generator.array.ConstantArrayGenerator;
import sagma.core.data.generator.field.ArrayFieldGenerator;
import sagma.core.data.generator.planetoid.DirectedFieldPlanetoidGenerator;
import sagma.core.data.generator.planetoid.PlanetoidGenerator;
import sagma.core.data.generator.zone.BiomeGenerator;
import sagma.core.material.Material;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.model.Sky;
import sagma.core.render.Game;

public class FPS extends Game {
	@Override
	public void init(GLAutoDrawable drawable) {
		setEscapeKeyToExit();
		setGameMode(GAMEMODE_3D);
		addDefaultKeyBindings();
		
		setCameraLocation(-0.7f, 0, 0.34f);
		setCameraRotation(-6.5f, -121, 0);
		
		float scale = 0.25f;
		int width = 400;
		int height = 400;
		
		Sky sky = null;
		try {
			sky = new Sky(new Vec3(0, 0, 0), 50.0f, "src/sagma/games/fps/resources/", "av9", ".jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		make(sky).setRotation(-90, 0, 0);
		
		BiomeGenerator gen = new BiomeGenerator(width, height);
		gen.generateNextPlanetoid("FPS", 1.0f);
		Instance i = make(gen);
		i.setScale(scale);
		
		PlanetoidGenerator planet = new DirectedFieldPlanetoidGenerator("PLANET",
				new ArrayFieldGenerator(new ConstantArrayGenerator(1.0f).nextArray2D()),
				Material.getMaterial("Texture/moon.jpg"));
		Instance j = make(planet);
		j.setPosition(0.23f, 0.24f, 0.21f);
		j.setScale(0.05f);
	}

}
