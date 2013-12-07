package sagma.games.rts.entity.ship;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.Color4f;
import sagma.core.math.Vec2;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.render.Game;
import sagma.core.render.Render;
import sagma.core.render.Text;
import sagma.games.rts.RTS;
import sagma.games.rts.client.Player;
import sagma.games.rts.emitters.DirectedEmitter;
import sagma.games.rts.emitters.Entity;
import sagma.games.rts.entity.projectile.Bullet;
import sagma.games.rts.sfx.RingOfFire;
import sagma.core.sound3d.Mixer3D;

public abstract class Ship extends Entity {
	public int resources;
	public float forwardThrust;
	public float backwardThrust;
	public float sidewardThrust;
	public float rotationThrust;
	public float boostThrust;
	public float maxSpeed;
	private Mixer3D sounds;
	private Text shipName = new Text("", 200, 200);

	public static final float DEFAULT_FORWARD_THRUST = 0.0046f;
	public static final float DEFAULT_BACKWARD_THRUST = 0.0036f;
	public static final float DEFAULT_SIDEWARD_THRUST = 0.0036f;
	public static final float DEFAULT_TURBO_BOOST = 0.03f;
	public static final float DEFAULT_TURBO_MAX_SPEED = 0.1f;
	public static final float DEFAULT_ROTATION_THRUST = 0.5f;
	public static final float DEFAULT_MAX_MOVEMENT_SPEED = 1;
	public static final float DEFAULT_MAX_ROTATION_SPEED = 0.8f;

	/**
	 * The smaller this number, the faster the ship slows down
	 */
	public static final float RESISTANCE_FACTOR = 0.888f;

	/**
	 * The smaller this number, the faster the ship's rotation slows down
	 */
	public static final float ROTATION_RESISTANCE_FACTOR = 0.885f;
	
	public Ship(String model, Player owner, Mixer3D sounds) {
		super(model);
		setLocation(0, 0, 1f);
		this.forwardThrust = DEFAULT_FORWARD_THRUST;
		this.backwardThrust = DEFAULT_BACKWARD_THRUST;
		this.sidewardThrust = DEFAULT_SIDEWARD_THRUST;
		this.rotationThrust = DEFAULT_ROTATION_THRUST;
		this.boostThrust = DEFAULT_TURBO_BOOST;
		this.sounds = sounds;
		DirectedEmitter gun = new DirectedEmitter(this, new Bullet(this), 
				new Vec3(0.08f, 0.25f, 0), new Vec3(0,0,0), new Vec3(0,0,0), 90f, 86f);
		gun.setRechargeRate(600);
		this.add(gun);
		DirectedEmitter gun2 = new DirectedEmitter(this, new Bullet(this), 
				new Vec3(-0.08f, 0.25f, 0), new Vec3(0,0,0), new Vec3(0,0,0),  90f, 86f);
		gun2.setRechargeRate(600);
		this.add(gun2);
		setScale(0.2f);
		this.owner = owner;
		
		RTS.textRender.add(shipName);
	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		gl.glDisable(GL.GL_DEPTH_TEST);
		super.draw(drawable);
		gl.glPopAttrib();
	}

	public void shoot() {
		for (Instance child : children) {
			if (child instanceof DirectedEmitter){
				((DirectedEmitter)child).shoot();
			}
		}
	}

	@Override
	public void heartbeat() {
		super.heartbeat();
		getRotationalSpeed().m_scale(ROTATION_RESISTANCE_FACTOR);
		getSpeed().m_scale(RESISTANCE_FACTOR);
		aim();
		
		if (networkName != null) shipName.setText(networkName);
		Vec2 pos = Render.getScreenCoordOfPoint(getPosition()).add(1, 1).scale(0.5f);
		shipName.setLocation((int)(pos.x*Render.WIDTH)-40, (int)(pos.y*Render.HEIGHT)+100);
	}

	public void aim() {
		if(((Player) owner).getMouseAdapter() != null){
			Vec2 glCoords = Game.getMousePositionNormalized();
			for (Instance child : children)
				if (child instanceof DirectedEmitter) 
					((DirectedEmitter)child).aim(glCoords);

		}
	}

	@Override
	public void destroy() {
		new RingOfFire(getLocation(), 0.01f, 100);

		Render.remove(this);
		RTS.soundSystem.playOnce(RTS.soundCollection.get("implode"));
	}

	public Mixer3D getSounds(){
		return sounds;
	}

	public void setOwner(Player owner){
		this.owner = owner;
	}
}
