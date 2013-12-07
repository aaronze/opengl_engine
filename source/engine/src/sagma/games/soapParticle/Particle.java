package sagma.games.soapParticle;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.generator.number.ConstantNumberGenerator;
import sagma.core.data.generator.planetoid.PlanetoidGenerator;
import sagma.core.data.generator.planetoid.RandomPlanetoidGenerator;
import sagma.core.event.CollisionAction;
import sagma.core.event.CollisionEventAction;
import sagma.core.material.Shader;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.render.Game;
import sagma.core.render.Render;

public class Particle extends Game{
	Vec3 GRAVITY = new Vec3(0, -0.001f, 0);
	float BALL_SCALE = 1f;
	
	Planet floor;
	PlanetoidGenerator gen;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		
		setEscapeKeyToExit();
		setGameMode(GAMEMODE_3D);
		addDefaultKeyBindings();
		Render.camera.getState().setLocation(new Vec3(1.9f, -2.1f, -2.4f));
		Render.camera.getState().setRotation(new Vec3(46, 33, 0));
		
		
		gen = new RandomPlanetoidGenerator("lava",new ConstantNumberGenerator(1.0f), 2);
		gen.setMaterial(new Shader("Shaders/Biome/lava"));
		gen.generateNextPlanetoid(BALL_SCALE);
		
		
		floor = new Planet(new RandomPlanetoidGenerator("planet",new ConstantNumberGenerator(0.5f), 5));
		add(floor);
		
		int xNum = 3;
		int yNum = 3;
		
		float size = 0.05f;
		
		float x = (size * (xNum - 1)) / 2;
		float y = (size * (yNum - 1)) / 2;
				
		
		for(int i = 0; i < xNum; i++) {
			for(int j = 0; j < yNum; j++) {
				for (int k = 0; k < 1; k++) {
					Ball obj = new Ball(gen);
					obj.getState().setLocation(new Vec3(j*size - x, k+1, i* size - y));
					obj.getState().addAcceleration(GRAVITY);
					obj.setScale(BALL_SCALE);
					add(obj);
				}
			}
		}
		
		
		add(new CollisionAction(Ball.class, Planet.class, new SphereCollidesWithFloor()));
		add(new CollisionAction(Ball.class, Ball.class, new BallsCollide()));
		
	}
	
	class Ball extends Instance {
		public Ball(PlanetoidGenerator generator) {
			super(generator);
		}
	}
	class Planet extends Instance {
		public Planet(PlanetoidGenerator generator) {
			super(generator);
			getState().setLocation(new Vec3(0, 0, 0));
		}
		
	}
	class BallsCollide implements CollisionEventAction {
		@Override
		public void actionEvent(Object a, Object b) {
			//System.out.println("!! Particle - SphereCollidesWithFloor - actionEvent()");
			Ball i = (Ball)a;
			Ball j = (Ball)b;
			
			Vec3 speed = i.getPosition().subtract(j.getPosition()).normalize();
			
			i.setSpeed(speed.scale(0.01f));
			j.setSpeed(speed.scale(-0.01f));
		}
	}
	
	class SphereCollidesWithFloor implements CollisionEventAction {
		@Override
		public void actionEvent(Object a, Object b) {
			//System.out.println("!! Particle - SphereCollidesWithFloor - actionEvent()");
			Ball i = (Ball)a;
			Instance j = (Instance)b;
			//Planet j = (Planet)b;
			
			//i.getState().setSpeed(i.getState().getSpeed().scale(-1.0f));
			i.setSpeed(i.getPosition().subtract(j.getPosition()).normalize().scale(0.05f));
		}
	}
}
