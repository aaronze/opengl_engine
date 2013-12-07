package sagma.games.rts.client;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import sagma.core.render.Game;
import sagma.games.rts.RTS;

public class PlayerMouseAdapter implements MouseListener, MouseWheelListener{

	private boolean mouse1 = false;
	private boolean mouse2 = false;
	private boolean mouse3 = false;
	
	private Player owner;
	
	public PlayerMouseAdapter(Player owner){
		this.owner = owner;
	}
	
	
	public void action(){
		// On macs a right click is essentially a control click
		// so control-click should not shoot.
		
		if(mouse1 && !Game.keyIsDown(Game.CONTROL)) {
			owner.ship.shoot();
		}
		if(mouse2){
			
		}
		if(mouse3){
			
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			mouse1 = true;
		}
		if(e.getButton() == MouseEvent.BUTTON2){
			mouse2 = true;
		}
		if(e.getButton() == MouseEvent.BUTTON3){
			mouse3 = true;
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			mouse1 = false;
		}
		if(e.getButton() == MouseEvent.BUTTON2){
			mouse2 = false;
		}
		if(e.getButton() == MouseEvent.BUTTON3){
			mouse3 = false;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		RTS.zoom(e.getWheelRotation());		
	}
	
}
