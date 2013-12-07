package sagma.games.tutorial7;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.generator.zone.BiomeGenerator;
import sagma.core.event.CollisionAction;
import sagma.core.event.CollisionEventAction;
import sagma.core.model.Instance;
import sagma.core.render.Game;

public class Tutorial7 extends Game {

	@Override
	public void init(GLAutoDrawable drawable) {
		setEscapeKeyToExit();
		setGameMode(GAMEMODE_3D);
		addDefaultKeyBindings();
		showFPS();
		
		setCameraLocation(0, 0, -10);
		
		BiomeGenerator gen = new BiomeGenerator(80, 80);
		gen.generateNextPlanetoid("gen",1.0f);
		add(new Planet(gen));
		
		BiomeGenerator gen2 = new BiomeGenerator(80, 80);
		gen2.generateNextPlanetoid("gen2",1.0f);
		Instance i = new Planet(gen2);
		i.setLocation(0, 4, 0);
		i.setAcceleration(0, -0.001f, 0);
		add(i);
		
		add(new CollisionAction(Planet.class, Planet.class, new PlanetCollides()));
		
		/*PlanetoidGenerator gen = new DirectedFieldPlanetoidGenerator("Texture/jeffTerran.jpg", "Texture/jeffTerran.jpg");
		make(gen);*/
	}
	
	class PlanetCollides implements CollisionEventAction {

		@Override
		public void actionEvent(Object a, Object b) {
			Instance i = (Instance)a;
			i.setSpeed(i.getSpeed().scale(-1));
		}
		
	}
	
	class Planet extends Instance {
		public Planet(BiomeGenerator gen) {
			super(gen);
			
		}
	}

}
