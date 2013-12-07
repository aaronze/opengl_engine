package sagma.games.rts.entity.cellestial;

import sagma.core.model.Instance;
import sagma.core.model.Model;

public class Moon extends Instance {
	public Moon() {
		super("moon");
		setLocation((float)Math.random()*20-10, (float)Math.random()*20-10, 1.0f);
		setSize(0.2f);
	}
	
	public Moon(Model m) {
		super(m);
	}
}
