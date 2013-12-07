package sagma.core.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileIO {
	public static ArrayList<String> read(File file) {
		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	public static void writeOver(File file, ArrayList<String> lines) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(file, false));
			for (String s : lines) {
				writer.println(s);
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeAppend(File file, ArrayList<String> lines) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(file, true));
			for (String s : lines) {
				writer.println(s);
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeAppend(File file, String line) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(file, true));
			writer.println(line);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] readAsByteArray(File file) {
		byte[] data = new byte[(int) file.length()];
		
		try {
			InputStream reader = new FileInputStream(file);
			int offset = 0;
			int readBytes = 0;
			while (offset < data.length && (readBytes=reader.read(data, offset, data.length-offset)) >= 0) {
				offset += readBytes;
			}
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
}
