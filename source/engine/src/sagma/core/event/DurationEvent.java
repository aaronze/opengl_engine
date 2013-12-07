package sagma.core.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class DurationEvent {
	Timer timerToStart;
	Timer timer;
	int max;
	int counter = 0;
	
	public DurationEvent(int start, int duration, final int interval, final WildcardEvent event) {
		max = duration;
		timer = new Timer(interval, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				event.eventRecieved();
				if ((counter += interval) >= max)
					timer.stop();
			}
		});
		if (start == 0) {
			start();
		} else {
			timerToStart = new Timer(start, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					start();
					timerToStart.stop();
				}
				
			});
			timerToStart.start();
		}
	}
	
	public void start() {
		timer.start();
	}
}
