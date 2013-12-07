package sagma.games.rts.entity.projectile;

import sagma.core.ai.InstanceController;
import sagma.core.data.generator.vector.*;
import sagma.core.data.structures.ThreadPool;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.particle.ParticleEmitter;
import sagma.core.render.Render;
import sagma.games.rts.RTS;
import sagma.games.rts.client.Client;
import sagma.games.rts.emitters.ExplosionSource;
import sagma.games.rts.entity.cellestial.Planet;
import sagma.games.rts.sfx.RingOfFire;

public class Bullet extends Projectile {
	public final static float BASE_SPEED = 0.09f;
	public final static int TIME_TO_LIVE = 100; 
	private int timeToLive = TIME_TO_LIVE;
	private ParticleEmitter emitter;
	private ExplosionSource bomb;

	public Bullet(Instance owner) {
		super("bullet", owner);
		setScale(0.02f);
	}

	public void addParticleEmitter() {
		emitter = new ParticleEmitter();
		emitter.setPosition(new Vec3(getLocation())); 
		emitter.setDirection(ParticleEmitter.RANDOM_DIRECTION);
		emitter.setParticlesReleasedPerSecond(40);
		emitter.setParticleDuration(50, 100);
		emitter.setColor(0.6f, 0.8f, 0.1f, 0.2f, 0.1f, 0.2f);
		emitter.setRotationSpeed(0, 0.1f, 0, 0.1f, 0, 0.1f);
		emitter.setSize(0.1f);
		ThreadPool.execute(emitter);
		
		Render.add(emitter);
	}

	public void addExplosion(){
		bomb = new ExplosionSource(this, .65f);
	}

	@Override
	public void destroy() {
		// Create fiery ring of death
		VectorGenerator initialPos = new VectorGenerator() {
			@Override
			public void setNextVector(Object caller, Vec3 v) {
				Vec3 pos = getLocation();
				float addX = (float)Math.random()*0.02f-0.01f;
				float addY = (float)Math.random()*0.02f-0.01f;
				v.set(pos.x+addX, pos.y+addY, pos.z);
			}
		};
		new RingOfFire(initialPos, 0.01f, 200);
		
		if(emitter != null){
			emitter.stop();
			emitter = null;
		}
		if(bomb != null){
			bomb.explode();
		}
		
		setVisible(false);
		setSolid(false);
		Render.remove(emitter);
		Render.remove(this);
	}

	@Override
	public void heartbeat() {
		RTS.client.update(this);
		if (timeToLive-- < 0) {
			Render.remove(this);
			emitter.stop();
			Render.remove(emitter);
			emitter = null;
			//destroy();
		}
	}
}
