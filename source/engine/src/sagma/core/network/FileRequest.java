package sagma.core.network;

import java.io.File;

public class FileRequest {
	private int priority;
	private File file;
	private String filename;
	private boolean isDownloaded = false;
	
	public String getFilename() {
		return filename;
	}
	
	public void download() {
		// TODO
		
	}
	
	public int getPriority() {
		return priority;
	}
	
	public File getFile() {
		return file;
	}
	
	public boolean isDownloaded() {
		return isDownloaded;
	}
}
