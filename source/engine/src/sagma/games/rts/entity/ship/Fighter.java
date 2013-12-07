package sagma.games.rts.entity.ship;

import sagma.core.ai.InstanceController;
import sagma.core.math.Vec3;
import sagma.core.sound3d.Mixer3D;
import sagma.games.rts.RTS;
import sagma.games.rts.client.Player;

public class Fighter extends Ship {
	private InstanceController ai;
	
	public Fighter(Player owner, Mixer3D sounds) {
		super(RTS.resources + "spaceship_asset.png", owner, sounds);
		setSize(0.1f);
		getState().setLocation(new Vec3((float)Math.random()*2-1, (float)(Math.random()*2-1), -1.0f));
		ai = new InstanceController(this);
		ai.setComfortRangeInner(0.3f);
		ai.addFlock(Fighter.class);
		ai.addPredator(CapitalShip.class);
		
		
	}
}
