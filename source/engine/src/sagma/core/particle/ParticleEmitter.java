package sagma.core.particle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.Timer;

import sagma.core.data.Color4f;
import sagma.core.data.generator.color.ColorGenerator;
import sagma.core.data.generator.color.ConstantColorGenerator;
import sagma.core.data.generator.number.ConstantNumberGenerator;
import sagma.core.data.generator.number.NumberGenerator;
import sagma.core.data.generator.number.RandomNumberGenerator;
import sagma.core.data.generator.vector.ConstantVectorGenerator;
import sagma.core.data.generator.vector.RandomVectorGenerator;
import sagma.core.data.generator.vector.VectorGenerator;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.model.State;
import sagma.core.render.Render;

/**
 * <p>
 * Contains all the information neccesary to continously produce a stream of particles.
 * Because of the infinite variations on particles, setting up a particle emitter is obviously complex.
 * </p>
 * 
 * <p> 
 * If you are looking for pre-generated particle emitters for common functions, consider using
 * one of the various special fx classes found in the "sagma.core.data.sfx" package.
 * </p>
 *  
 * <p>
 * The first thing worth noting is that there are two main ways of creating an emitter.
 * The first way is to define a particle state, and leave everything you want defined by the state as null.
 * The second, and more simpler way is to leave the state as null and define absolutely everything else.
 * </p>
 * 
 * <p><h2><b>
 * State
 * </b></h2></p>
 * 
 * <p>
 * Defining a state is useful as a backup information source. If you leave any other information undefined
 * then the information will be retrieved from the particle state. In most cases however you will not need
 * to worry about it. Likely you will just leave it as null.
 * </p>
 * 
 * <p><h2><b>
 * Position
 * </b></h2></p>
 * 
 * <p>
 * The second component, position, is the location of the particle emitter, and thus the initial 
 * location of all particles created. While this may sound like a horrible restriction if you want, 
 * for example, to have particles being created in a cube around a point, it is worth noting that
 * such a mechanism is entirely possible and is in the capable hands of the user. One possible implementation
 * of such a creational-cube would look like:
 * </p>
 * 
 * <i>
 * <br>float x = 120.0f;
 * <br>float y = -60.0f;
 * <br>float z = 10.0f;
 * <br>float size = 12.0f / 2.0f;
 * <br>VectorGenerator position;
 * <br>position = new RandomVectorGenerator(x-size, x+size, y-size, y+size, z-size, z+size);
 * </i>
 * 
 * <p>
 * Where (x, y, z) is the position of the centre of the cube and size is half the size of the cube.
 * </p>
 * 
 * <p><h2><b>
 * Direction
 * </b></h2></p>
 * 
 * <p>
 * The third component is direction. Direction must not be confused with the direction of travel.
 * This component does not control which direction the object moves when a force is applied.
 * This component controls the rotation of the individual particles. Rotation is important because the
 * particles are made up of triangles. Typically the initial direction will be random, and you can 
 * easily set this using:
 * <i>
 * <br>VectorGenerator direction = ParticleEmitter.RANDOM_DIRECTION;
 * </i>
 * </p>
 * 
 * <p><h2><b>
 * Quantity Per Second
 * </b></h2></p>
 * 
 * <p>
 * This is how many particles should be released every second. It will automatically release them
 * gradually spanning the second, rather then in one big block every second.
 * </p>
 * 
 * <p><h2><b>
 * Duration
 * </b></h2></p>
 * 
 * <p>
 * This is the lifespan of the particle in milliseconds. Note that like most other variables this too can be
 * modified dynamically. For a couple of examples, if you wanted a random lifespan between 1 and 2 seconds
 * you could use the following:
 * <i>
 * <br>NumberGenerator lifespan = new RandomNumberGenerator(1000, 2000);
 * </i>
 * </p>
 * 
 * <p>
 * For another example if you wanted the particles to last 1 second initially but over time gradually
 * get shorter and shorter lives until they are no longer created, you could use:
 * <i>
 * <br>NumberGenerator lifespan = new LinearNumberGenerator(1000, -0.1f, 0);
 * </i>
 * </p>
 * 
 * <p><h2><b>
 * Color
 * </b></h2></p>
 * 
 * <p>
 * Defines the color variation scheme to be used. See the documentation at ColorGenerator for
 * more information.
 * </p>
 * 
 * <p><h2><b>
 * Speed
 * </b></h2></p>
 * 
 * <p>
 * This component defines how the particle will start moving when created.
 * Typically this component is left alone and movement is controlled using acceleration vectors.
 * Thus most cases can just use:
 * <i>
 * <br>VectorGenerator speed = ParticleEmitter.RANDOM_SPEED;
 * </i>
 * </p>
 * 
 * <p><h2><b>
 * Gravity
 * </b></h2></p>
 * 
 * <p>
 * This is the key component of the particle engine.
 * Gravity controls the complex motions of the particle after particle creation.
 * There are two main ways of creating gravity. 
 * The first is the cheap approximation and the second is point gravity.
 * </p>
 * 
 * <p>
 * For the common case, that is, gravity going in a downwards direction you can use the default:
 * <i>
 * <br>VectorGenerator gravity = ParticleEmitter.DEFAULT_GRAVITY;
 * </i>
 * </p>
 * 
 * <p>
 * If you need a stronger or weaker gravity then you can easily define your own:
 * <i>
 * <br>VectorGenerator gravity = new ConstantVectorGenerator(0.0f, -10.0f, 0.0f);
 * </i>
 * </p>
 * 
 * <p>
 * For most particle systems however you will be defining a different gravity and the
 * way you should be defining it is with Gravity Vectors. 
 * </p>
 * 
 * <p>
 * For example, assuming 'y = 0' is the plane at which objects in the scene are placed around
 * and the radius of the 'simulated planet' is 10,000 then we can use the following gravity vector 
 * to achieve the same result as with the approximation above:
 * <i>
 * <br>VectorGenerator gravity = new GravityVectorGenerator(
 * <br>&nbsp	new ConstantVectorGenerator(0.0f, -10000.0f, 0.0f),
 * <br>&nbsp	new ConstantNumberGenerator(10.0f));
 * </i>
 * 
 * <p>
 * Note that in the above gravity vector the gravity is 10 and not -10 as before. This is
 * because a positive number represents movement towards the source of gravity.
 * </p>
 * 
 * <p>
 * Both the approximation and point gravity can be used to create much more complex effects by 
 * using combinations of specialized vector generators. Of course, it should be noticed, however,
 * that the user can make their own vector generator subclasses to define gravity, or indeed
 * any other generated value.
 * </p>
 * 
 * <p>
 * For an interesting and complete example, say there is a gravity flip game where the gravity could 
 * either be upwards or downwards. To represent the current direction of gravity you decide to
 * use a boolean called 'isGravityDown' in you Game class called 'Game'. You decide to show the 
 * player the direction of the current gravity by randomly generating golden particles on the field
 * around the player's position and letting them be affected by gravity like everything else in the game.
 * </p>
 * 
 * <p>
 * <i>
 * <br>ParticleState state = null; 
 * <br>
 * <br>VectorGenerator position = new VectorGenerator() {
 * <br>&nbsp	public Vec3 nextVector(Object caller) {
 * <br>&nbsp&nbsp		Vec3 pos = Game.getPlayerPosition();
 * <br>&nbsp&nbsp		float fieldX = 1000.0f;
 * <br>&nbsp&nbsp		float fieldZ = 1000.0f;
 * <br>&nbsp&nbsp		float fieldYStart = 0.0f;
 * <br>&nbsp&nbsp		float fieldYEnd = 200.0f;
 * <br>&nbsp&nbsp		float xValue = new RandomNumberGenerator(pos.x - fieldX, pos.x + fieldX).nextNumber();
 * <br>&nbsp&nbsp		float yValue = new RandomNumberGenerator(fieldYStart, fieldYEnd).nextNumber();
 * <br>&nbsp&nbsp		float zValue = new RandomNumberGenerator(pos.z - fieldZ, pos.z + fieldZ).nextNumber();
 * <br>&nbsp&nbsp		return new Vec3(xValue, yValue, zValue);
 * <br>&nbsp	}
 * <br>}
 * <br>
 * <br>VectorGenerator direction = ParticleEmitter.RANDOM_DIRECTION;
 * <br>int rate = 2000;
 * <br>NumberGenerator lifespan = new ConstantNumberGenerator(200);
 * <br>ColorGenerator color = ConstantColorGenerator(Color4f.GOLD);
 * <br>VectorGenerator speed = new ConstantVectorGenerator(0, 0, 0);
 * <br>
 * <br>VectorGenerator gravity = new VectorGenerator() {
 * <br>&nbsp	private static final UP_GRAVITY = new ConstantVectorGenerator(0, 10, 0);
 * <br>&nbsp	private static final DOWN_GRAVITY = new ConstantVectorGenerator(0, -10, 0);	
 * <br>&nbsp	public Vec3 nextVector(Object caller) {
 * <br>&nbsp&nbsp		if (Game.isGravityDown) return UP_GRAVITY;
 * <br>&nbsp&nbsp		return DOWN_GRAVITY;	
 * <br>%nbsp	}
 * <br>}
 * <br>
 * <br>VectorGenerator rotationSpeed = ParticleEmitter.RANDOM_SPEED;
 * <br>VectorGenerator torque = new ConstantVectorGenerator(0, 0, 0);
 * <br>float mass = 1.0f;
 * <br>float inertia = 1.0f;
 * <br>float size = 0.1f;
 * </i>
 * </p>
 * 
 * @author Aaron Kison
 *
 */

public class ParticleEmitter extends Instance implements Runnable {
	public final static VectorGenerator RANDOM_DIRECTION = new RandomVectorGenerator(360, 360, 360);
	public final static VectorGenerator RANDOM_SPEED = new RandomVectorGenerator(-1, 1, -1, 1, -1, 1);
	public final static VectorGenerator DEFAULT_GRAVITY = new ConstantVectorGenerator(new Vec3(0, -0.1f, 0));
	public final static ColorGenerator RANDOM_COLOR = new ColorGenerator(new RandomVectorGenerator(1, 1, 1));

	private LinkedBlockingQueue<Particle> particles;
	private State initialState;
	int particlesPerSecond;
	private NumberGenerator particleLife;
	private ColorGenerator color;
	private VectorGenerator acceleration;
	private VectorGenerator initialSpeed;
	private VectorGenerator initialRotationalSpeed;
	private VectorGenerator initialTorque;
	private VectorGenerator initialPosition;
	private VectorGenerator initialDirection;
	private float size;
	private float inertia;
	private float mass;
	
	private Timer stepTimer, addTimer, flushTimer;

	public ParticleEmitter() {
		defaultSettings();
		init();
	}
	
	public ParticleEmitter(State state) {
		initialState = state;
		init();
	}
	
	/**
	 * Replaced by:
	 * <p><ul>
	 * <li>{@link #ParticleEmitter() ParticleEmitter}
	 * <li>{@link #ParticleEmitter(State) ParticleEmitter(State)}
	 * </ul></p>
	 * 
	 * 
	 * @param initialState Leave as null unless otherwise needed
	 * @param initialPosition
	 * @param initialDirection
	 * @param quantityReleasedPerSecond
	 * @param particleDurationInMilliSeconds
	 * @param particleColor
	 * @param initialSpeed
	 * @param gravity
	 * @param initialRotationalSpeed
	 * @param initialTorque
	 * @param mass
	 * @param inertia
	 * @param size
	 */
	@Deprecated
	public ParticleEmitter(State initialState, VectorGenerator initialPosition, 
			VectorGenerator initialDirection,  int quantityReleasedPerSecond, 
			NumberGenerator particleDurationInMilliSeconds, ColorGenerator particleColor, 
			VectorGenerator initialSpeed, VectorGenerator gravity, 
			VectorGenerator initialRotationalSpeed, VectorGenerator initialTorque, 
			float mass, float inertia, float size) {

		this.initialState = initialState;
		this.initialRotationalSpeed = initialRotationalSpeed;
		this.initialTorque = initialTorque;
		this.initialSpeed = initialSpeed;
		this.mass = mass;
		this.inertia = inertia;
		this.initialPosition = initialPosition;
		this.initialDirection = initialDirection;
		particlesPerSecond = quantityReleasedPerSecond;
		particleLife = particleDurationInMilliSeconds;
		color = particleColor;
		this.size = size;
		acceleration = gravity;

		init();
	}
	

	private void init() {
		setSolid(false);
		setPickable(false);
		if (initialPosition == null) initialPosition = new ConstantVectorGenerator(initialState.getPosition());
		if (initialDirection == null) initialDirection = new ConstantVectorGenerator(initialState.getRotation());
		if (particlesPerSecond == 0) particlesPerSecond = 100;
		if (particleLife == null) particleLife = new ConstantNumberGenerator(100);
		if (color == null) color = new ConstantColorGenerator(Color4f.WHITE);
		if (initialSpeed == null) initialSpeed = new ConstantVectorGenerator(initialState.getSpeed());
		if (acceleration == null) acceleration = new ConstantVectorGenerator(initialState.getAcceleration());
		if (initialRotationalSpeed == null) initialRotationalSpeed = new ConstantVectorGenerator(initialState.getRotationalSpeed());
		if (initialTorque == null) initialTorque = new ConstantVectorGenerator(initialState.getTorque());
		if (size == 0) size = 0.1f;
		if (inertia == 0) inertia = 0.01f;
		if (mass == 0) mass = 0.01f;

		particles = new LinkedBlockingQueue<Particle>();

		stepTimer = new Timer(20, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				step();
			}

		});

		addTimer = new Timer(1000/particlesPerSecond, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (int i = particlesPerSecond/1000; i >= 0; i--)
					createNewParticle();
			}

		});

		flushTimer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				flush();
			}

		});
	}

	void flush() {
		Iterator<Particle> iterator = particles.iterator();

		while (iterator.hasNext()) {
			Particle p = iterator.next();
			if (!p.isAlive()) {
				particles.remove(p);
			}
		}
	}
	
	public void add(Particle p) {
		particles.add(p);
	}
	
	public void createNewParticle(Vec3 v1, Vec3 v2, Vec3 v3) {
		State s = new State(initialPosition, initialSpeed, acceleration, new ConstantNumberGenerator(mass),
				initialDirection, initialRotationalSpeed, initialTorque, new ConstantNumberGenerator(inertia));
		Particle p = new Particle(s, color.nextColor(), size, particleLife.nextNumber());
		p.setTriangle(v1, v2, v3);
		particles.add(p);
	}

	void createNewParticle() {
		State s = new State(initialPosition, initialSpeed, acceleration, new ConstantNumberGenerator(mass),
				initialDirection, initialRotationalSpeed, initialTorque, new ConstantNumberGenerator(inertia));
		Particle p = new Particle(s, color.nextColor(), size, particleLife.nextNumber());
		particles.add(p);
	}

	@Override
	public void step() {
		Iterator<Particle> iterator = particles.iterator();
		while (iterator.hasNext()) {
			Particle p = iterator.next();
			acceleration.setNextVector(p, p.state().getAcceleration());
			p.step();
		}
	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		if (Render.renderPass == Render.RENDER_DRAW) {
			GL2 gl = drawable.getGL().getGL2();
	
			gl.glPushMatrix();
			gl.glTranslatef(getLocation().x, getLocation().y, getLocation().z);
	
			Iterator<Particle> iterator = particles.iterator();
			while (iterator.hasNext()) {
				iterator.next().draw(drawable);
			}
	
			gl.glPopMatrix();
		}
	}
	
	private void defaultSettings() {
		initialPosition = new ConstantVectorGenerator(new Vec3(0, 0, 0));
		initialDirection = RANDOM_DIRECTION;
		particlesPerSecond = 100;
		particleLife = new ConstantNumberGenerator(100);
		color = new ConstantColorGenerator(Color4f.WHITE);
		initialSpeed = new ConstantVectorGenerator(new Vec3(0, 0, 0));
		acceleration = new ConstantVectorGenerator(new Vec3(0, 0, 0));
		initialRotationalSpeed = new ConstantVectorGenerator(new Vec3(0, 0, 0));
		initialTorque = new ConstantVectorGenerator(new Vec3(0, 0, 0));
		size = 0.1f;
		inertia = 0.01f;
		mass = 0.01f;
	}
	
	@Override
	public void setSize(float i) {size = i;}
	@Override
	public void setScale(float i) {size = i;}
	public void setInertia(float i) {inertia = i;}
	public void setMass(float i) {mass = i;}
	
	public void setParticlesReleasedPerSecond(int i) {particlesPerSecond = i;}
	
	public void setParticleDuration(float i) {setParticleDuration(new ConstantNumberGenerator(i));}
	public void setParticleDuration(float min, float max) {setParticleDuration(new RandomNumberGenerator(min, max));}
	public void setParticleDuration(NumberGenerator v) {particleLife = v;}
	
	@Override
	public void setPosition(float x, float y, float z) {setPosition(new Vec3(x, y, z));}
	@Override
	public void setPosition(Vec3 v) {setPosition(new ConstantVectorGenerator(v));}
	public void setPosition(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		setPosition(new RandomVectorGenerator(xMin, xMax, yMin, yMax, zMin, zMax));}
	public void setPosition(VectorGenerator v) {initialPosition = v;};
	
	@Override
	public void setLocation(float x, float y, float z) {setPosition(new Vec3(x, y, z));}
	@Override
	public void setLocation(Vec3 v) {setPosition(new ConstantVectorGenerator(v));}
	public void setLocation(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		setLocation(new RandomVectorGenerator(xMin, xMax, yMin, yMax, zMin, zMax));}
	public void setLocation(VectorGenerator v) {initialPosition = v;};
	
	@Override
	public void setSpeed(float x, float y, float z) {setSpeed(new Vec3(x, y, z));}
	@Override
	public void setSpeed(Vec3 v) {setSpeed(new ConstantVectorGenerator(v));}
	public void setSpeed(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		setSpeed(new RandomVectorGenerator(xMin, xMax, yMin, yMax, zMin, zMax));}
	public void setSpeed(VectorGenerator v) {initialSpeed = v;};
	
	@Override
	public void setVelocity(float x, float y, float z) {setSpeed(new Vec3(x, y, z));}
	@Override
	public void setVelocity(Vec3 v) {setSpeed(new ConstantVectorGenerator(v));}
	public void setVelocity(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		setVelocity(new RandomVectorGenerator(xMin, xMax, yMin, yMax, zMin, zMax));}
	public void setVelocity(VectorGenerator v) {initialSpeed = v;};
	
	@Override
	public void setAcceleration(float x, float y, float z) {setAcceleration(new Vec3(x, y, z));}
	@Override
	public void setAcceleration(Vec3 v) {setAcceleration(new ConstantVectorGenerator(v));}
	public void setAcceleration(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		setAcceleration(new RandomVectorGenerator(xMin, xMax, yMin, yMax, zMin, zMax));}
	public void setAcceleration(VectorGenerator v) {acceleration = v;};
	
	public void setGravity(float x, float y, float z) {setAcceleration(new Vec3(x, y, z));}
	public void setGravity(Vec3 v) {setAcceleration(new ConstantVectorGenerator(v));}
	public void setGravity(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		setGravity(new RandomVectorGenerator(xMin, xMax, yMin, yMax, zMin, zMax));}
	public void setGravity(VectorGenerator v) {acceleration = v;};
	
	@Override
	public void setRotation(float x, float y, float z) {setRotation(new Vec3(x, y, z));}
	@Override
	public void setRotation(Vec3 v) {setRotation(new ConstantVectorGenerator(v));}
	public void setRotation(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		setRotation(new RandomVectorGenerator(xMin, xMax, yMin, yMax, zMin, zMax));}
	public void setRotation(VectorGenerator v) {initialDirection = v;};
	
	@Override
	public void setDirection(float x, float y, float z) {setRotation(new Vec3(x, y, z));}
	@Override
	public void setDirection(Vec3 v) {setRotation(new ConstantVectorGenerator(v));}
	public void setDirection(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		setDirection(new RandomVectorGenerator(xMin, xMax, yMin, yMax, zMin, zMax));}
	public void setDirection(VectorGenerator v) {initialDirection = v;};
	
	public void setRotationSpeed(float x, float y, float z) {setRotationSpeed(new Vec3(x, y, z));}
	public void setRotationSpeed(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		setRotationSpeed(new RandomVectorGenerator(xMin, xMax, yMin, yMax, zMin, zMax));}
	public void setRotationSpeed(Vec3 v) {setRotationSpeed(new ConstantVectorGenerator(v));}
	public void setRotationSpeed(VectorGenerator v) {initialRotationalSpeed = v;};
	
	public void setTorque(float x, float y, float z) {setTorque(new Vec3(x, y, z));}
	public void setTorque(Vec3 v) {setTorque(new ConstantVectorGenerator(v));}
	public void setTorque(VectorGenerator v) {initialTorque = v;};
	
	public void setColor(float x, float y, float z) {setColor(new Color4f(x, y, z));}
	public void setColor(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		setColor(new ColorGenerator(new RandomVectorGenerator(xMin, xMax, yMin, yMax, zMin, zMax)));}
	public void setColor(Color4f color) {setColor(new ConstantColorGenerator(color));}
	public void setColor(ColorGenerator v) {color = v;};
	
	public void stop() {
		stepTimer.stop();
		flushTimer.stop();
		addTimer.stop();
	}

	public int getSize() {
		return particles.size();
	}

	@Override
	@Deprecated
	public void run() {
		stepTimer.start();
		flushTimer.start();
		addTimer.start();
	}
}
