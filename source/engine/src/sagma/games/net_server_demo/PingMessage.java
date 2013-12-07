package sagma.games.net_server_demo;

/**
 * @category Network Message
 * @author Mitch
 *
 */
public class PingMessage {
	
	public PingMessage ( ) {
		this.i = -1;
	}
	
	public PingMessage(int i) {
		this.i = i;
	}
	
	public int i;
	
}