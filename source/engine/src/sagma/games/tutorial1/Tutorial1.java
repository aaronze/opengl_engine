package sagma.games.tutorial1;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.input.KeyBind;
import sagma.core.model.Instance;
import sagma.core.render.Game;
import sagma.core.render.Render;

public class Tutorial1 extends Game {
	Instance player;
	float MOVE_SPEED = 0.1f;

	@Override
	public void init(GLAutoDrawable drawable) {
		setEscapeKeyToExit();
		setGameMode(GAMEMODE_2D);
		
		player = make("Models/fern.obj");
		player.setSize(0.1f);
		Render.add(player);
		
		add(new MoveRight());
		add(new MoveLeft());
	}

	class MoveRight extends KeyBind {
		@Override
		public boolean isKey(int key) {
			if (key == RIGHT) return true;
			if (key == D) return true; 
			return false;
		}

		@Override
		public void keyPressed() {
			player.addSpeed(MOVE_SPEED, 0, 0);
		}

		@Override
		public void keyReleased() {
			player.addSpeed(-MOVE_SPEED, 0, 0);
		}
		
	}
	
	class MoveLeft extends KeyBind {
		@Override
		public boolean isKey(int key) {
			if (key == LEFT) return true;
			if (key == A) return true; 
			return false;
		}

		@Override
		public void keyPressed() {
			player.addSpeed(-0.1f, 0, 0);
		}

		@Override
		public void keyReleased() {
			player.addSpeed(0.1f, 0, 0);
		}
		
	}
}
