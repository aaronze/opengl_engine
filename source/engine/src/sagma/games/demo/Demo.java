package sagma.games.demo;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.input.KeyBind;
import sagma.core.io.RadialMenu;
import sagma.core.io.RadialMenuItem;
import sagma.core.model.Model;
import sagma.core.render.Game;

/**
 * 
 * @author Aaron Kison
 *
 */
public class Demo extends Game {
	RadialMenu menu;
	Model model;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		setGameMode(GAMEMODE_2D);
		setEscapeKeyToExit();
		
		model = makeModel("Texture/mario.png");
		
		add(new LPressed());
	}
	
	class LPressed extends KeyBind {

		@Override
		public boolean isKey(int key) {
			return key == L;
		}

		@Override
		public void keyPressed() {
			menu.add(new RadialMenuItem(model));
		}

		@Override
		public void keyReleased() {
		}
		
	}
}
