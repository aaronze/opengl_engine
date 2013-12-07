package sagma.games.net_server_demo;

/**
 * @category Network Message
 * @author Mitch
 *
 */
public class PongMessage {
	
	public PongMessage ( ) {
		this.i = -1;
	}
	
	public PongMessage(int i) {
		this.i = i;
	}
	
	public int i;
	
}