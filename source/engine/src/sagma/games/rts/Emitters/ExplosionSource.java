package sagma.games.rts.emitters;

import sagma.core.model.Instance;
import sagma.core.render.Render;
import sagma.games.rts.RTS;

/**
 * This is intended to act as the source of an explosion wherein a bubble will spawn, 
 * grow to a maximum size and disappear.
 * The location of the explosion will either be at the location of the parent when explode()
 * is called, or it may be completely independent.
 * 
 * @author Michael
 *
 */
public class ExplosionSource extends Instance {
	Instance parent;
	
	private float maxScale;
	public static final float DEFAULT_GROWTH_SPEED = 0.064f;
	
	public ExplosionSource(Instance parent, float maxScale){
		super("exPLOUD");
		this.parent = parent;
		this.maxScale = maxScale;
		
		this.setVisible(false);
		this.setSolid(false);
		this.setScale(0.2f);
	}
	
	public float getMaxScale() {
		return maxScale;
	}

	public void setMaxScale(float maxScale) {
		this.maxScale = maxScale;
	}

	@Override
	public void heartbeat(){
		if(true){
			setScale(getScale()+DEFAULT_GROWTH_SPEED);
			if(getScale() > maxScale){
				Render.remove(this);
				setVisible(false);
			}
		}
	}
	
	public void explode(){
		if(parent != null){
			setLocation(parent.getLocation().add(this.getLocation()));
		}
		Render.add(this);
		setVisible(true);
		
		RTS.soundSystem.playOnce(RTS.soundCollection.get("explode"));
	}
	
}
