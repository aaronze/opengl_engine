import java.util.ArrayList;
import java.io.File;

public class Manifest {
	public ArrayList<String> list = new ArrayList<String>();
	File root = null;

	public Manifest(String root) {
		this.root = new File(root);
		addFolder(new File(root));
	}
	
	public void addFolder(File file) {
		addFile(file);
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			for (File f : children) {
				addFolder(f);
			}
		}
	}
	
	public void addFile(File file) {
		String s = file.getPath().replace(root.getPath(), "") + " " + file.length();
		list.add(s);
	}
	
	public String toString() {
		String s = "";
		for (String string : list) {
			s += string + "|";
		}
		return s;
	}
}