package sagma.core.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A comparable list of files
 * 
 * @author Aaron Kison
 *
 */
public class Manifest {
	private ArrayList<FileInfo> files;
	
	public Manifest(ArrayList<FileInfo> files) {
		this.files = files;
	}

	public static Manifest make() {
		File root = new File("src/sagma/games/");
		
		ArrayList<FileInfo> fileList = new ArrayList<FileInfo>();
		addRecursive(fileList, root);
		
		Manifest manifest = new Manifest(fileList);
		return manifest;
	}
	
	public static void addRecursive(ArrayList<FileInfo> fileList, File file) {
		// Add parent
		fileList.add(FileInfo.makeFromFile(file));
		
		// Add children
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			for (int i = children.length-1; i >= 0; i--) {
				addRecursive(fileList, children[i]);
			}
		}
	}
	
	public ArrayList<FileInfo> getFiles() {
		return files;
	}
	
	public ArrayList<FileInfo> getChangesFrom(Manifest server) {
		ArrayList<FileInfo> changes = new ArrayList<FileInfo>();
		
		Iterator<FileInfo> serverFiles = server.getFiles().iterator();
		while (serverFiles.hasNext()) {
			FileInfo file = serverFiles.next();
			
			// Attemp to find equivilant on client
			FileInfo clientFile = find(file);
			
			if (clientFile == null || !clientFile.equals(file)) {
				// File has changed
				changes.add(clientFile);
			}
		}
		
		return changes;
	}
	
	public FileInfo find(FileInfo file) {
		Iterator<FileInfo> clientFiles = files.iterator();
		String filename = file.getFilename();
		while (clientFiles.hasNext()) {
			FileInfo clientFile = clientFiles.next();
			if (filename == clientFile.getFilename()) {
				return clientFile;
			}
		}
		return null;
	}
}
