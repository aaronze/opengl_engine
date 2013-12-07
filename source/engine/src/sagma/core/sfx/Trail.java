package sagma.core.sfx;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.generator.vector.*;
import sagma.core.data.structures.ThreadPool;
import sagma.core.math.Vec3;
import sagma.core.particle.ParticleEmitter;
import sagma.core.render.Drawable;

public class Trail implements Drawable {
	final Vec3 start = new Vec3(0,0,0);
	final Vec3 end = new Vec3(0,0,0);
	private ParticleEmitter generator;
	
	public Trail(final VectorGenerator pathStart, final VectorGenerator pathEnd) {
		VectorGenerator pos = new VectorGenerator() {
			@Override
			public void setNextVector(Object caller, Vec3 v) {
				pathStart.setNextVector(caller, start);
				pathEnd.setNextVector(caller, end);
				
				float dist = start.subtract(end).length();
				float segment = (float)(Math.random()*dist);
				
				v.set(start.add(end.normalize().scale(segment)));
			}
    	};
    	
    	generator = new ParticleEmitter();
    	generator.setPosition(pos);
    	generator.setDirection(ParticleEmitter.RANDOM_DIRECTION);
    	generator.setParticlesReleasedPerSecond(50000);
    	generator.setParticleDuration(200, 300);
    	generator.setColor(0.85f, 1.0f, 0.7f, 0.88f, 0.0f, 0.15f);
    	generator.setSpeed(0, 0.1f, 0);
    	generator.setRotationSpeed(0, 0.1f, 0, 0.1f, 0, 0.1f);
    	generator.setSize(5.0f);
    	ThreadPool.execute(generator);
	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		generator.draw(drawable);
	}
	
	public void stop() {
		generator.stop();
	}
	
}
