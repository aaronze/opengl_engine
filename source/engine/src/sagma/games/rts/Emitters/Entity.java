package sagma.games.rts.emitters;

import sagma.core.model.Constructable;
import sagma.core.model.Instance;
import sagma.core.model.Model;

public abstract class Entity extends Instance {
	public final static float DEFAULT_MAX_HEALTH = 100;
	public final static float DEFAULT_REGEN = 0.01f;
	private float regen = DEFAULT_REGEN;
	private float maxHealth = DEFAULT_MAX_HEALTH;
	private float health;
	private boolean isAlive = true;
	
	public Entity(String m) {
		super(m);
		
		health = maxHealth;
	}
	
	public Entity(Model m) {
		super(m);
		
		health = maxHealth;
	}
	
	public Entity(Constructable c) {
		super(c);
		
		health = maxHealth;
	}
	
	public void setMaxHealth(float val) {
		float dif = val-maxHealth;
		if (val > maxHealth) {
			health += dif;
		} else {
			health = val;
		}
		maxHealth = val;
	}
	
	public void damage(float val) {
		health -= val;
	}
	
	public void heal(float val) {
		health += val;
	}
	
	public float getHealth() {
		return health;
	}
	
	@Override
	public void heartbeat() {
		if (!isAlive()) return;
		if (health < 0) {
			setAlive(false);
			destroy();
		}
		heal(regen);
	}
	
	public void destroy() {
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
}
