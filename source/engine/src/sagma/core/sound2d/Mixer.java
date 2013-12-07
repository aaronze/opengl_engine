package sagma.core.sound2d;

public class Mixer extends Thread {
	@Override
	public void run() {
		
	}
	
	public void playOnce(SoundClip clip) {
		clip.playOnce();
	}
	
	public void playLoop(SoundClip clip) {
		clip.playLoop();
	}
}
