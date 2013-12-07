package sagma.games.testAsset;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.media.opengl.GLAutoDrawable;
import javax.swing.JFileChooser;
import sagma.core.input.KeyBind;
import sagma.core.model.Instance;
import sagma.core.render.Game;

public class TestAsset extends Game {
	Instance asset;
	float scale = 0.5f;

	@Override
	public void init(GLAutoDrawable drawable) {
		setEscapeKeyToExit();
		setGameMode(GAMEMODE_3D);
		
		File root = new File("engine/");
		JFileChooser fc = new JFileChooser(root);
		fc.showOpenDialog(new Frame());
		File selected = fc.getSelectedFile();
		if (selected != null) {
			String s = selected.getPath();
			System.out.println(s);
			asset = make(s);
			asset.setScale(scale);
		}
		
		
		add(new MoveUp());
		add(new MoveDown());
	}
	
	private class MoveUp extends KeyBind {
		@Override
		public boolean isKey(int key) {
			return key == KeyEvent.VK_W || key == KeyEvent.VK_UP;
		}
		@Override
		public void keyPressed() {
			scale *= 1.2f;
			asset.setScale(scale);
		}
		@Override
		public void keyReleased() {
			
		}
	}
	
	private class MoveDown extends KeyBind {
		@Override
		public boolean isKey(int key) {
			return key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN;
		}
		@Override
		public void keyPressed() {
			scale *= 0.8f;
			asset.setScale(scale);
		}
		@Override
		public void keyReleased() {
			
		}
	}

}
