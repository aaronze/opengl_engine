package sagma.core.ai.learning;

import java.util.ArrayList;

public class LearningEngine {
	private ArrayList<Neuron> neurons;
	
	public LearningEngine() {
		neurons = new ArrayList<Neuron>();
	}
	
	public void addRule(ArrayList<Rule> rules) {
		addRule(rules.toArray(new Rule[rules.size()]));
	}
	
	public void addRule(Rule[] rules) {
		for (Rule rule : rules) {
			addRule(rule);
		}
	}
	
	public void addRule(Rule rule) {
		int[] freq = rule.getFrequencies();
		Neuron[] neuronList = new Neuron[freq.length];
		
		for (int i = 0; i < freq.length; i++) {
			neuronList[i] = getNeuronWithFrequency(freq[i]);
			if (neuronList[i] == null) {
				neuronList[i] = new Neuron(freq[i]);
				neurons.add(neuronList[i]);
			}
		}
		
		for (int i = 0; i < freq.length; i++) {
			Neuron n = neuronList[i];
			for (int j = i+1; j < freq.length; j++) {
				Neuron m = neuronList[j];
				n.addConnected(m, rule.getCoherence());
				m.addConnected(n, rule.getCoherence());
			}
		}
	}
	
	public Neuron getNeuronWithFrequency(int frequency) {
		for (Neuron n : neurons) {
			if (n.getFrequency() == frequency) return n;
		}
		return null;
	}
	
	public Neuron getBestNeuron(Rule rule) {
		Neuron neuron = null;
		int bestCoherence = 0;
		
		// Get list of neurons
		int[] freqs = rule.getFrequencies();
		ArrayList<Neuron> list = new ArrayList<Neuron>();
		for (int i = 0; i < freqs.length; i++) {
			list.add(getNeuronWithFrequency(freqs[i]));
		}
		
		// Compile list of shared connected
		if (list.size() < 1) return null;
		Neuron con = list.get(0);
		if (con == null) return null;
		ArrayList<Neuron> connected = con.getConnectedNeurons();
		ArrayList<Integer> coherence = con.getCoherence();
		for (int i = 0; i < connected.size(); i++) {
			if (coherence.get(i) < 0) continue;
			Neuron n = connected.get(i);
			// Check if n exists in all shared connections
			bestCoherence = 1;
			for (Neuron m : list) {
				if (m == null) continue;
				if (m.getConnectedNeurons() == null) continue;
				int index = m.getConnectedNeurons().indexOf(n);
				if (index < 0) {
					bestCoherence = 0;
					break;
				}
			}
			if (bestCoherence == 1) {
				neuron = n;
			}
		}
		
		return neuron;
	}
}
