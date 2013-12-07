package sagma.core.ai.learning;

import java.util.ArrayList;

public class Neuron {
	private int frequency;
	private ArrayList<Neuron> connected;
	private ArrayList<Integer> coherence;
	
	public Neuron(int freq) {
		frequency = freq;
		connected = new ArrayList<Neuron>();
		coherence = new ArrayList<Integer>();
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public void addConnected(Neuron n, int cohere) {
		if (connected.contains(n)) {
			addCoherence(connected.indexOf(n), cohere);
		} else {
			connected.add(n);
			coherence.add(cohere);
		}
	}
	
	public void addCoherence(int index, int value) {
		int i = coherence.get(index).intValue();
		i += value;
		coherence.set(index, new Integer(i));
	}
	
	public ArrayList<Neuron> getConnectedNeurons() {
		return connected;
	}
	
	public ArrayList<Integer> getCoherence() {
		return coherence;
	}
}
