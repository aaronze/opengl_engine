package sagma.games.tutorial5;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.generator.array.RandomArrayGenerator;
import sagma.core.data.generator.zone.Zone;
import sagma.core.render.Game;

public class Tutorial5 extends Game {

	@Override
	public void init(GLAutoDrawable drawable) {
		setEscapeKeyToExit();
		setGameMode(GAMEMODE_3D);
		
		PLAYER_SPEED = 0.1f;

		setCameraLocation(0, -0.5f, 0);
		setCameraRotation(0, 130, 0);
		
		Zone zone = new Zone(new RandomArrayGenerator(0.0f, 1.0f, 200, 200));
		zone.generateNextZone(0.1f);
		make(zone);
	}
	
	@Override
	public void heartbeat() {
		if (keyIsDown(W))
			moveCameraForward();
		if (keyIsDown(A))
			moveCameraLeft();
		if (keyIsDown(S))
			moveCameraBackward();
		if (keyIsDown(D))
			moveCameraRight();
		
		if (keyIsDown(SHIFT)) 
			moveCameraDown();
		if (keyIsDown(SPACE))
			moveCameraUp();
	}

}
