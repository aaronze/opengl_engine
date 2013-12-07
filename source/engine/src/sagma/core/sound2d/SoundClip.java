package sagma.core.sound2d;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class SoundClip {
	String filename;
	boolean stopped = false;
	
	public SoundClip(String filename) {
		this.filename = filename;
	}
	
	public void playOnce() {
		new Thread() {
			@Override
			public void run() {
				SourceDataLine soundLine = null;
			      int BUFFER_SIZE = 64*1024;  // 64 KB
			   
			      // Set up an audio input stream piped from the sound file.
			      try {
			         File soundFile = new File(filename);
			         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			         AudioFormat audioFormat = audioInputStream.getFormat();
			         DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
			         soundLine = (SourceDataLine) AudioSystem.getLine(info);
			         soundLine.open(audioFormat);
			         soundLine.start();
			         int nBytesRead = 0;
			         byte[] sampledData = new byte[BUFFER_SIZE];
			         while (nBytesRead != -1) {
			            nBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
			            if (nBytesRead >= 0) {
			               // Writes audio data to the mixer via this source data line.
			               soundLine.write(sampledData, 0, nBytesRead);
			            }
			         }
			         soundLine.drain();
			         soundLine.close();
			      } catch (Exception ex) {
			         ex.printStackTrace();
			      }
			}
		}.start();
	}
	
	public void playLoop() {
		new Thread() {
			@Override
			public void run() {
				while (!stopped) playOnceAndWait();
			}
		}.start();
	}
	
	public void playOnceAndWait() {
		SourceDataLine soundLine = null;
	      int BUFFER_SIZE = 64*1024;  // 64 KB
	   
	      // Set up an audio input stream piped from the sound file.
	      try {
	         File soundFile = new File(filename);
	         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
	         AudioFormat audioFormat = audioInputStream.getFormat();
	         DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	         soundLine = (SourceDataLine) AudioSystem.getLine(info);
	         soundLine.open(audioFormat);
	         soundLine.start();
	         int nBytesRead = 0;
	         byte[] sampledData = new byte[BUFFER_SIZE];
	         while (nBytesRead != -1) {
	            nBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
	            if (nBytesRead >= 0) {
	               // Writes audio data to the mixer via this source data line.
	               soundLine.write(sampledData, 0, nBytesRead);
	            }
	         }
	         soundLine.drain();
	         soundLine.close();
	      } catch (Exception ex) {
	         ex.printStackTrace();
	      }
	}
	
	public void stop() {
		stopped = true;
	}

}
