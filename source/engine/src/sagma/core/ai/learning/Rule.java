package sagma.core.ai.learning;

public class Rule {
	private int[] frequencies;
	private int coherence;
	
	public Rule(int[] freqs, int coherence) {
		frequencies = freqs;
		this.coherence = coherence;
	}
	
	public int[] getFrequencies() {
		return frequencies;
	}
	
	public int getCoherence() {
		return coherence;
	}
}
