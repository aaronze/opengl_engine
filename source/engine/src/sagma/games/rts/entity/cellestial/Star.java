package sagma.games.rts.entity.cellestial;

import java.awt.event.MouseEvent;

import sagma.core.data.generator.number.RandomNumberGenerator;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.render.Render;
import sagma.games.rts.RTS;

public class Star extends Instance {
	private final static float MIN_SIZE = 0.004f * 2;
	private final static float MAX_SIZE = 0.031f * 2;
	private float size;
	private final static RandomNumberGenerator random = new RandomNumberGenerator(MIN_SIZE, MAX_SIZE);
	
	public Star() {
		super("Star");
		setSolid(false);
		setPickable(false);
		size = random.nextNumber();
		setSize(size);
		setLocation((float)Math.random()*2*RTS.MAX_ZOOM-RTS.MAX_ZOOM, (float)Math.random()*2*RTS.MAX_ZOOM-RTS.MAX_ZOOM, -10f);
		this.setPickable(MouseEvent.BUTTON3, true);
	}
	
	@Override
	public void heartbeat() {
		Vec3 cameraPos = Render.camera.getLocation();
		Vec3 starPos = getLocation();
		
		Vec3 playerSpeed = RTS.getPlayer(0).ship.getSpeed();
		float scaler = 0.1f;
		setSpeed(playerSpeed.x*scaler, playerSpeed.y*scaler, playerSpeed.z*scaler);
		
		float camX = -cameraPos.x;
		float camY = -cameraPos.y;
		float starX = starPos.x;
		float starY = starPos.y;
		
		if (starX < (camX - RTS.MAX_ZOOM)) 
			starX += RTS.MAX_ZOOM*2;
		else if (starX > (camX + RTS.MAX_ZOOM)) 
			starX -= RTS.MAX_ZOOM*2;
		else if (starY < (camY - RTS.MAX_ZOOM)) 
			starY += RTS.MAX_ZOOM*2;
		else if (starY > (camY + RTS.MAX_ZOOM)) 
			starY -= RTS.MAX_ZOOM*2;
		
		setLocation(starX, starY, starPos.z);
	}
}
