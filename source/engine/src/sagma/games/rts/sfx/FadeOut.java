package sagma.games.rts.sfx;

import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.games.rts.RTS;
import sagma.games.rts.client.Player;

/**
 * Acts as the bounds for the game
 * -- BUGS --
 * - a white line appears on the inner boundary of the image... which is kinda alright but should be fixed
 * - bullets don't show up under the fade out image... which is kinda alright but should be fixed
 * 
 * @author Michael
 *
 */
public class FadeOut extends Instance{
	Player parent;
	
	public static final int LEFT = 1;
	public static final int TOP = 2;
	public static final int RIGHT = 3;
	public static final int BOTTOM = 4;
	
	int orientation;
	
	public FadeOut(Player parent, int orientation){
		super("fadeOut");
		this.parent = parent;
		this.orientation = orientation;
		if(orientation == LEFT){
			this.setRotation(new Vec3(0,0,0));
			this.setPosition(-RTS.MAP_WIDTH, parent.ship.getPosition().y, 3.5f);
		}
		else if(orientation == TOP){
			this.setRotation(new Vec3(0, 0, 270f));
			this.setPosition(parent.ship.getPosition().x, RTS.MAP_HEIGHT, 3.5f);
		}
		else if(orientation == RIGHT){
			this.setRotation(new Vec3(0, 0, 180f));
			this.setPosition(RTS.MAP_WIDTH, parent.ship.getPosition().y, 3.5f);
		}
		else if(orientation == BOTTOM){
			this.setRotation(new Vec3(0, 0, 90f));
			this.setPosition(parent.ship.getPosition().x, -RTS.MAP_HEIGHT, 3.5f);
		}
		setScale(14f);
		setSolid(false);
	}
	
	@Override
	public void heartbeat(){
		if(orientation == LEFT){
			this.setPosition(-RTS.MAP_WIDTH, parent.ship.getPosition().y, 3.5f);
		}
		else if(orientation == TOP){
			this.setPosition(parent.ship.getPosition().x, RTS.MAP_HEIGHT, 3.5f);
		}
		else if(orientation == RIGHT){
			this.setPosition(RTS.MAP_WIDTH, parent.ship.getPosition().y, 3.5f);
		}
		else if(orientation == BOTTOM){
			this.setPosition(parent.ship.getPosition().x, -RTS.MAP_HEIGHT, 3.5f);
		}
	}
	
	
	
}
