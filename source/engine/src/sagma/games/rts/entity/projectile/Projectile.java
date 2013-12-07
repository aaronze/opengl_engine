package sagma.games.rts.entity.projectile;

import sagma.core.event.CollisionEventAction;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.model.Model;
import sagma.core.render.Render;
import sagma.games.rts.emitters.Entity;

public class Projectile extends Instance {
	public final static float DEFAULT_DAMAGE = 10.0f;
	
	private float damage;
	private Instance owner;
	
	public Projectile(Model m){
		super(m);
	}
	
	public Projectile(String m, Instance owner) {
		super(m);
		this.owner = owner;
		setPickable(false);
		//Source of bug when map is scrollable
		getState().setLocation(new Vec3(0, 0, 1f));
		damage = DEFAULT_DAMAGE;
	}
	
	public Projectile(Model m, Instance owner) {
		super(m);
		this.owner = owner;
		setPickable(false);
		//Source of bug when map is scrollable
		getState().setLocation(new Vec3(0, 0, 1f));
		damage = DEFAULT_DAMAGE;
	}
	
	public Instance getOwner() {
		return owner;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public static class collidesWithPlanet implements CollisionEventAction {
		@Override
		public void actionEvent(Object a, Object b) {
			Projectile bullet = (Projectile)a;
			Entity ship = (Entity)b;
			
			// If ship is not owner
			if (ship == bullet.getOwner()) return;
			
			// Cause damage to the ship
			ship.damage(bullet.getDamage());
			
			// Destroy the bullet
			bullet.destroy();
		}
		
	}
	
	public void destroy() {
		Render.remove(this);
	}
}
