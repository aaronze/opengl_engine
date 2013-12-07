package sagma.core.io;

import java.io.File;

public class FileGetter {
	public static String root = "src/sagma/";
	
	{
		File file = new File(root);
		if (!file.exists()) root = "sagma/";
	}
	
	public static File getFile(String path) {
		return new File(root + path);
	}
}
