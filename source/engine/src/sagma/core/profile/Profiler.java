package sagma.core.profile;

import java.util.ArrayList;

public class Profiler {
	private static ArrayList<Record> records = new ArrayList<Record>();
	
	private static class Record {
		long startTime = 0;
		long totalTime = 0;
		int counter = 0;
		String name;
		public Record(String name) {
			this.name = name;
		}
		
		public void addTime(long time) {
			totalTime += time;
			counter++;
		}
		
		@Override
		public String toString() {
			long avg = totalTime;
			if (counter == totalTime) {
				return name + " : \t" + totalTime;
			}
			if (counter != 0) 
				avg = totalTime / counter;
			return name + " : \t" + (totalTime/1000000) + " ms, \tAvg-Time: " + avg + " ns";
		}
	}
	
	public static void reset(String name) {
		for (Record r : records) {
			if (r.name.equals(name)) {
				r.totalTime = 0;
				r.startTime = System.nanoTime();
				return;
			}
		}
	}
	
	public static void start(String name) {
		for (Record r : records) {
			if (r.name.equals(name)) {
				r.startTime = System.nanoTime();
				return;
			}
		}
		
		
		Record r = new Record(name);
		r.startTime = System.nanoTime();
		records.add(r);
	}
	
	public static void incremenet(String name) {
		for (Record r : records) {
			if (r.name.equals(name)) {
				r.addTime(1);
				return;
			}
		}
		
		Record record = new Record(name);
		records.add(record);
		record.addTime(1);
	}
	
	public static void stop(String name) {
		long nowTime = System.nanoTime();
		
		for (Record r : records) {
			if (r.name.equals(name)) {
				long duration = nowTime-r.startTime;
				r.addTime(duration);
				return;
			}
		}
		
		throw new RuntimeException(name + " was not started!");
	}
	
	public static String print() {
		String s = "";
		while (!records.isEmpty()) {
			long highest = 0;
			Record high = null;
			for (Record r : records) {
				if (r.totalTime > highest) {
					highest = r.totalTime;
					high = r;
				}
			}
			if (high != null) {
				s += high.toString() + "\n";
				records.remove(high);
			}
		}
		return s;
	}
}
