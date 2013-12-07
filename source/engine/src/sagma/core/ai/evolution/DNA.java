package sagma.core.ai.evolution;

/**
 * <p><class>DNA</class> represents a sequence of DNA for use in evolutionary
 * artificial intelligence.</p>
 *
 * <p>Each DNA is comprised of a string of information and a score.
 * <br/>The score represents how well the DNA performs in a particular environment.
 * <br/>The score determines the strongest DNA of the species and the strongest
 *  is usually chosen as the mating partner for the next generation.</p>
 *
 * @version 0.3
 * @author Aaron Kison
 */
public class DNA implements Comparable<DNA> {
    private int[] dna;
    private double score = -Double.MAX_VALUE;

    /**
     * Constructs a new DNA strand using the given DNA information string
     *
     * @param p String of DNA to be used
     */
    public DNA(int[] p) {
        dna = p;
    }

    public DNA() {

    }

    /**
     * Returns the string of DNA contained in this DNA object.
     *
     * @return String of DNA
     */
    public int[] getDNA() {
        return dna;
    }

    /**
     * <p>Returns the associated score for this DNA.
     * <br/>If no score has been assigned, the score is set to be 0.</p>
     *
     * @return
     */
    public double getScore() {
        return score;
    }

    /**
     * <p>Sets the score for this DNA strand.
     * <br/>The score should be set to a positive number to be used
     * <br/>Setting the score to negative levels can result in strange performance of the AI
     *  where hopefully the DNA strand will just be culled from existence. Much worse can happen though</p>
     *
     * <p><b>Do NOT set a negative score</b></p>
     *
     * @param d Positive score to be set
     */
    public void setScore(double d) {
        score = d;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < dna.length; i++) s += dna[i] + ", ";
        return s + " : " + score;
    }

    public void setDNA(int[] mutate) {
        dna = mutate;
    }

	@Override
	public int compareTo(DNA o) {
		double s = o.getScore();
        if (score > s) return 1;
        if (score < s) return -1;
        return 0;
	}
}

