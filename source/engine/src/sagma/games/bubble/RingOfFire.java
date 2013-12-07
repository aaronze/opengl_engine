package sagma.games.bubble;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import sagma.core.data.generator.color.ColorGenerator;
import sagma.core.data.generator.vector.RandomVectorGenerator;
import sagma.core.data.generator.vector.VectorGenerator;
import sagma.core.data.structures.ThreadPool;
import sagma.core.math.Vec3;
import sagma.core.particle.ParticleEmitter;
import sagma.core.render.Render;

public class RingOfFire {
	ParticleEmitter emitter;
	Timer timer;
	Timer delTimer;
	
	public RingOfFire(Vec3 location, final float size, int msDuration) {
		this(location, size, msDuration, new ColorGenerator(
				new RandomVectorGenerator(0.8f, 1.0f, 0.1f, 1.0f, 0.1f, 0.2f)));
	}
	
	public RingOfFire(Vec3 location, final float size, int msDuration, ColorGenerator colorGen) {
		VectorGenerator direction = new VectorGenerator() {

			@Override
			public void setNextVector(Object caller, Vec3 vec) {
				Vec3 speed = new RandomVectorGenerator(-1, 1, -1, 1, -1, 1).nextVector(this);
				speed.m_normalize();
				speed.m_scale(size);
				vec.set(speed);
			}
			
		};
		
		emitter = new ParticleEmitter();
		emitter.setLocation(location.x, location.y, location.z);
		emitter.setParticlesReleasedPerSecond((int)(20000000*size));
		emitter.setParticleDuration(10000*size);
		emitter.setColor(colorGen);
		emitter.setSpeed(direction);
		emitter.setRotationSpeed(0, 0.1f, 0, 0.1f, 0, 0.1f);
		emitter.setSize(0.01f);
		ThreadPool.execute(emitter);
		
		
		emitter.getState().setLocation(location.x, location.y, location.z);
		Render.add(emitter);
		
		timer = new Timer(msDuration, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				emitter.stop();
				timer.stop();
				emitter = null;
				timer = null;
			}
			
		});
		
		delTimer = new Timer((int)(msDuration + 10000*size), new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Render.remove(emitter);
				delTimer.stop();
				delTimer = null;
			}
			
		});
		
		timer.start();
		delTimer.start();
	}
}
