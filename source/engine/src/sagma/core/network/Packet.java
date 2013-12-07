package sagma.core.network;

import java.util.ArrayList;

public class Packet {
	public final static int SIZE_OF_A_BYTE = 8;
	public final static int SIZE_OF_A_CHAR = 2 * SIZE_OF_A_BYTE;
	public final static int CHAR_CAPACITY = 20;
	public final static int BYTE_CAPACITY = SIZE_OF_A_CHAR * CHAR_CAPACITY;
	
	private ArrayList<String> lines;
	private int size;
	
	public final static Packet OK = new Packet().write("OK");
	
	public Packet() {
		lines = new ArrayList<String>();
		size = 0;
	}
	
	public Packet write(String s) {
		lines.add(s);
		
		size += s.length() * SIZE_OF_A_CHAR;
		
		return this;
	}
	
	public ArrayList<String> getLines() {
		return lines;
	}
	
	public String toString() {
		String s = "";
		for (String a : lines) {
			s += a + "\n";
		}
		return s;
	}
	
	public boolean isFull() {
		return size >= BYTE_CAPACITY;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean equals(Object o) {
		return toString().equals(o.toString());
	}
}
