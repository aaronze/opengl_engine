package sagma.core.network;

import java.io.File;

import sagma.core.data.Manifest;

/**
 * 
 * @author onlycheats
 */
public class Patcher extends Thread {
	private Manifest clientManifest;
	private Manifest serverManifest;
	
	public Patcher() {
		start();
	}
	
	public void run() {
		// Get client manifest
		clientManifest = Manifest.make();
		
		// Download manifest from server
		serverManifest = downloadManifest();
		
		// Compare manifest to server
		//ArrayList<FileInfo> differentFiles = clientManifest.getChangesFrom(serverManifest);
		
		// Add files to be updated
		
	}
	
	public Manifest downloadManifest() {
		// TODO
		return null;
	}
	
	public File downloadFile(String filename) {
		// TODO
		return null;
	}
	
	public Manifest getClientManifest() {
		return clientManifest;
	}
	
	public Manifest getServerManifest() {
		return serverManifest;
	}
}
