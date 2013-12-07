package sagma.core.data;

import java.io.File;
import java.util.ArrayList;

import sagma.core.io.FileIO;

/**
 * Represents a file
 * 
 * @author Aaron Kison
 *
 */
public class FileInfo {
	private String filename;
	private long size;
	private long hash;
	
	public FileInfo(String name, long size, long hash) {
		filename = name;
		this.size = size;
		this.hash = hash;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public long getSize() {
		return size;
	}
	
	public long getHash() {
		return hash;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass() == FileInfo.class) {
			FileInfo info = (FileInfo)o;
			return (info.getFilename().equals(filename)) && 
					(info.getSize() == size) && (info.getHash() == hash);
		}
		return false;
	}
	
	public static FileInfo makeFromFile(File file) {
		String filename = file.getPath();
		long size = file.length();
		long hash = hash(file);
		
		return new FileInfo(filename, size, hash);
	}
	
	public static long hash(File file) {
		long hash = file.length() * file.getName().length();
		return hash;
	}
	
	public static ArrayList<String> getFileLines(File file) {
		return FileIO.read(file);
	}
}
