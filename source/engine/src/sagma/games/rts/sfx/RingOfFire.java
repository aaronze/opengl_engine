package sagma.games.rts.sfx;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import sagma.core.data.generator.vector.ConstantVectorGenerator;
import sagma.core.data.generator.vector.RandomVectorGenerator;
import sagma.core.data.generator.vector.VectorGenerator;
import sagma.core.data.structures.ThreadPool;
import sagma.core.math.Vec3;
import sagma.core.particle.ParticleEmitter;
import sagma.core.render.Render;

public class RingOfFire {
	ParticleEmitter emitter;
	Timer timer;
	public final RandomVectorGenerator gen = new RandomVectorGenerator(-1,1,-1,1,-1,1);
	
	public RingOfFire(Vec3 location, final float size, int msDuration) {
		this(new ConstantVectorGenerator(location), size, msDuration);
	}
	
	public RingOfFire(VectorGenerator location, final float size, int msDuration) {
		VectorGenerator direction = new VectorGenerator() {

			@Override
			public void setNextVector(Object caller, Vec3 vec) {
				gen.setNextVector(this, vec);
				vec.m_normalize();
				vec.m_scale(size);
			}
			
		};
		
		emitter = new ParticleEmitter();
		emitter.setLocation(location);
		emitter.setParticlesReleasedPerSecond(20000);
		emitter.setParticleDuration(msDuration);
		emitter.setColor(0.8f, 1.0f, 0.1f, 1.0f, 0.1f, 0.2f);
		emitter.setSpeed(direction);
		emitter.setRotationSpeed(0, 0.1f, 0, 0.1f, 0, 0.1f);
		emitter.setSize(0.01f);
		ThreadPool.execute(emitter);
		
		Render.add(emitter);
		
		timer = new Timer(msDuration, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				emitter.stop();
				timer.stop();
				emitter = null;
				timer = null;
				Render.remove(emitter);
			}
			
		});
		timer.start();
	}
}
