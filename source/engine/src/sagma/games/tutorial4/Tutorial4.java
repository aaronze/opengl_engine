package sagma.games.tutorial4;

import java.io.File;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.model.Instance;
import sagma.core.render.Game;

public class Tutorial4 extends Game {
	private String gameDirectory = "src/sagma/games/tutorial4/";

	@Override
	public void init(GLAutoDrawable drawable) {
		setEscapeKeyToExit();
		setGameMode(GAMEMODE_2D);
		
		File marioImage = new File(gameDirectory + "/resources/mario.png");
		generateMaskFor(marioImage);
		
		make("Texture/alpine_north.bmp");
		
		Instance mario = make(gameDirectory + "/resources/mario.png");
		mario.setSize(0.1f);
		mario.setLocation(0, 0, -0.1f);
		add(mario);
	}

}
