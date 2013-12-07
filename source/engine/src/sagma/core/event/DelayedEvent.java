package sagma.core.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class DelayedEvent {
	Timer timer;
	public DelayedEvent(long delay, final WildcardEvent e) {
		timer = new Timer((int)delay, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				e.eventRecieved();
				timer.stop();
			}
			
		});
		timer.start();
	}
}
