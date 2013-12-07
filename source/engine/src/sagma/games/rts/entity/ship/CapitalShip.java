package sagma.games.rts.entity.ship;

import sagma.core.data.model.Model;
import sagma.core.sound3d.Mixer3D;
import sagma.games.rts.RTS;
import sagma.games.rts.client.Player;

public class CapitalShip extends Ship {
	public CapitalShip(Player owner, Mixer3D sounds) {
		super("capitalShip", owner, sounds);
		setScale(0.05f);
	}
}
