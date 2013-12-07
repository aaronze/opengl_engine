package sagma.games.tutorial3;

import java.awt.event.MouseEvent;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.model.Pickable;
import sagma.core.event.PickedObjectAction;
import sagma.core.event.PickedObjectEvent;
import sagma.core.event.PickedObjectEventAction;
import sagma.core.model.*;
import sagma.core.render.Game;

public class Tutorial3 extends Game {
	float fieldWidth = 20;
	float fieldHeight = 20;
	Model field;

	@Override
	public void init(GLAutoDrawable drawable) {
		setEscapeKeyToExit();
		setGameMode(GAMEMODE_2D);
		
		field = makeModel("Models/fern.obj");
		
		float startX = -1.0f;
		float startY = -1.0f;
		float tileWidth = 2.0f / fieldWidth;
		float tileHeight = 2.0f / fieldHeight;
		
		for (int x = 0; x < fieldWidth; x++) {
			for (int y = 0; y < fieldHeight; y++) {
				float xPos = startX + tileWidth*x;
				float yPos = startY + tileHeight*y;
				Block block = new Block();
				block.setLocation(xPos, yPos, 0);
				block.setPickable(MouseEvent.BUTTON3, true);
				add(block);
			}
		}
		
		add(new PickedObjectAction(Block.class, new BlockSelected()));
	}
	
	class Block extends Instance {
		public Block() {
			super(field);
			setSize(1.0f / fieldWidth);
		}
	}
	
	class BlockSelected extends PickedObjectEventAction {
		@Override
		public void eventRecieved(Pickable p, PickedObjectEvent e) {
			Instance i = (Instance)p;
			if(i.isPickable(MouseEvent.BUTTON3) && e.getButton()==MouseEvent.BUTTON3){
				i.setVisible(false);
				i.setPickable(MouseEvent.BUTTON3, false);
				i.setPickable(MouseEvent.BUTTON1, false);
				System.out.println(i);
			}
			if(i.isPickable(MouseEvent.BUTTON1) && e.getButton()==MouseEvent.BUTTON1){
				System.out.println(i);
				i.setVisible(false);
			}
			
			
		}
		
	}
	
	@Override
	public void heartbeat() {
	}
	
}
