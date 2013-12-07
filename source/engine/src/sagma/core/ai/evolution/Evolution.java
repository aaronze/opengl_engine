package sagma.core.ai.evolution;

import sagma.core.data.generator.number.RandomNumberGenerator;

/**
 * <p><class>Evolution</class> is a genetic based system for gaining
 *  and simulating intelligence by evolving a genetic sequence over time using
 *  different parameters to control how the system evolves genetically.</p>
 *
 * <p>The default methods allow for an equal chance genetic evolution that should
 *  work well in almost any instance, though not specifically optimized for
 *  any particular case. </p>
 *
 * <p>Due to the number of possible variable changes there are two solutions for
 *  evolutionary optimization:</p>
 *
 * <p><ui>
 * <li> - Manually tweak the parameters following the general guidelines listed in
 *  each of their comments, whilst watching out for the intelligence leaks which
 *  is specified in the comments of each parameter</li>
 *
 * <li> - Create a secondary evolutionary <class>Evolution</class> to
 *  monitor and adapt the first <class>Evolution</class> to the best
 *  possible parameters.</li>
 * </ui></p>
 *
 * @version 1.0
 * @author Aaron Kison
 */
public class Evolution {
	private static RandomNumberGenerator rand100;
	
    /**
     * <p>A percentage chance (between 0 and 100) that each time a new generation is born
     *  its DNA will undergo an insertion mutation.
     * <br/>An insertion mutation is a
     *  mutation where a random module is inserted into a random location in the DNA.</p>
     *
     *  <p><b>Warning</b>: Setting this to 0 will force the DNA never to grow in size and thus complexity</p>
     */
    public final static int INSERTION = 1;
    /**
     * <p>A percentage chance (between 0 and 100) that each time a new generation is born
     *  its DNA will undergo a deletion mutation.
     * <br/>A deletion mutation is a
     *  mutation where a small chunk of DNA will be deleted at a random location</p>
     *
     *  <p><b>Warning</b>: Setting this to 0 will not allow the DNA to shrink in size, it will have no
     *   tolerance for adaptability unless there is a null action in the action list</p>
     */
    public final static int DELETION = 2;
    /**
     * <p>A percentage chance (between 0 and 100) that each time a new generation is born
     *  its DNA will undergo an alteration mutation. An alteration mutation is a
     *  mutation where a small chunk of DNA at a random location will be changed to
     *  another chunk of random DNA.</p>
     *
     *  <p><b>Warning</b>: Setting this to 0 will mean exponential longer times for generations to get
     *   things right</p>
     *
     *  <p><b>Warning</b>: Setting this to 90+ will be very unstable. Good for very unstable environments
     *   and very bad for everything else. A high alteration chance mixes well with a high
     *   birth rate of at least 100.</p>
     */
    public final static int ALTERATION = 3;
    /**
     * <p>Chance that the best parent's {@link DNA DNA} will be chosen over the second best parent's DNA</p>
     *
     * <p>When normal {@link ArtificialIntelligence#MEIOSIS MEIOSIS} occurs, each parent's DNA is mixed equally to form the children
     * <br/>When normal {@link ArtificialIntelligence#MITOSIS MITOSIS} occurs, only on parent's DNA is copied unchanging to form the child</p>
     *
     * <p><ul>
     * <li>A bias of 0 simulates complete MEIOSIS</li>
     * <li>A bias of 50+ simulated complete MITOSIS</li>
     * <li>A bias between 0 and 50 is a mixture of both</li>
     * <li>A negative bias is deliberate sabotage, where the best parent's DNA is deliberately
     *  tossed out in favor of the second best DNA.</li>
     * <li>A bias of -50 and more is complete MITOSIS of the second best parent's DNA</li>
     * </ul></p>
     */
    public final static int PARENT_BIAS = 4;
    /**
     * <p>The number of offspring the parents will have in each generation</p>
     *
     * <p>Set low for stable environments where adaptability is not a major concern.
     * <br/>Set high for dynamic environments where change occurs frequently</p>
     *
     * <p>This number should be a direct reflection on the number of possible actions.
     * <br/>If a create can move on left and right, this number should be about 4.
     * <br/>If its a fully modeled human with 1000 muscle movements, this number should be
     *  about 2000</p>
     *
     * <p>Thus (2 * amount of actions) is a good number to start with</p>
     */
    public final static int CHILDREN_GENERATED = 5;
    /**
     * <p>A process in which the {@link DNA DNA} of the parents is combined to form a child</p>
     *
     * <p>DNA is sliced into many 'Chunks'. Each 'chunk' consists of one piece
     * of information. These 'chunks' are <b>randomly combined from both parents</b>.</p>
     *
     * <p>Specifically, if the parents have the same information in a chunk,
     *  then all children WILL have that same information. If the chunk information
     *  of the parents differ, then the chunk is inherited from either parent
     *  using a coin-flip approach.</p>
     *
     * <p>Using {@link ArtificialIntelligence#MEIOSIS meiosis} alongside a small number of children is a good way to ensure
     *  <b>DNA is constantly changing</b> between generations and thus the DNA system is
     *  <b>more adaptable</b> and accepting of major changes in the environment.<p>
     *
     * <p>Meiosis is only meaningful if the two <b>parents are varied in DNA</b>. This variance
     *  is <b>not possible with a large number of children</b>, because the two parents are
     *  chosen by their superior DNA, which is likely to be <b>too similar for true
     *  meiosis</b>, thus using {@link ArtificialIntelligence#MITOSIS mitosis} instead with a much smaller number of children
     *  is better suited.</p>
     */
    public final static int MEIOSIS = 0;
    /**
     * The initial bestLength of the DNA (excluding delimiters, only key information points)
     *
     * This can be changed by using {@link ArtificialIntelligence#set(int, int) set(LENGTH, <bestLength>)}
     */
    public final static int LENGTH = 6;
    /**
     * <p>A process in which the {@link DNA DNA} of a parent copies itself to form a child</p>
     *
     * <p>In this process only the DNA of a <b>single parent</b> is copied to a child,
     *  thus making all children a <b>clone of the original</b>.</p>
     *
     * <p>Using this, in conjuction <b>with a relatively large amount of children</b>
     *  (more then 50) will reach an optimum state for a <b>stable system</b> very quickly.</p>
     *
     * <p>Using mitosis with a small amount of children (less then 5) will lend itself
     *  to <b>random progression</b> and the DNA 'guessing' which way is best. In a <b>dynamic
     *  environment this will fail entirely,</b>. <b>Do not use a small number of children.</b></p>
     *
     * <p><b>Warning</b>: Do not use mitosis for a dynamic environment with changing factors
     *  as a mitosis based generation process will suffer consequences</p>
     */
    public final static int MITOSIS = 50;

    public static int INSERTION_CHANCE = 20;
    public static int DELETION_CHANCE = 20;
    public static int ALTERATION_CHANCE = 20;
    public static int NUMBER_OF_CHILDREN = 1000;
    public static int BEST_PARENT_BIAS = 0;
    public static int LENGTH_OF_DNA = 10;
    public static int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    private DNA[] pathStore;
    private ChildCruncher[] crunchers;
    private static int[] actions;

    /**
     * <p>Creates a new <class>ArtificialIntelligence</class> that will evolve over
     * time to create optimized DNA with the given action set.</p>
     *
     * <p>The action list given should be a <b>string delimited list of possible actions</b>
     * The first action, should have a value of 0 and be a null action, that is,
     * an action that does nothing. This will allow the DNA to eventually grow into
     * a stable state that will adapt well to mutations instead of constantly
     * changing its operation.</p>
     *
     * <p>An <b>example</b> of such an action list:
     * <br/> - String actionList = "0, TURN_LEFT, TURN_RIGHT, JUMP, DUCK";</p>
     *
     * <p>With the appropriate mapping this can easily become:
     * <br/> - String actionList = "0, 1, 2, 3, 4";</p>
     *
     * <p>Of course the delimiter can be anything you want in any order
     * <br/> - String actionList = "0aaa1aaa2aaa3aaa4";
     * <br/>where "a" would be used as the delimiter string as in:
     * <br/> - new ArtificialIntelligence(actionList, "a");</p>
     *
     * <p>This also means the delimiter CANNOT be part of the list as in:
     * <br/> - String actionList = "0aaaBACK_AWAYaaaMOVE_FORWARD"
     * <br/>because this will be interpreted as:
     * <br/> - String[] actions = 0, B, CK_, W, Y, MOVE_FORW, RD</p>
     *
     * <p>Thus the best delimiters tend to be uncommonly used characters
     *  such as a single space " " or a comma ","</p>
     *
     * <p>Note that if you use ", " as a delimiter, this means that "," and " " will
     * both be used individually, so that all these will be interpreted the same:
     * <br/> - String actionList = "0,JUMP,DUCK";
     * <br/> - String actionList = "0 JUMP DUCK";
     * <br/> - String actionList = "0, JUMP, DUCK";
     * <br/>while this will be interpreted incorrectly:
     * <br/> - String actionList = "0, TURN LEFT, TURN RIGHT"</p>
     *
     * <p>Thus if you want to use spaces, make sure to not have a space in the delimiter</p>
     *
     * @param actionList String specifying all possible actions, delimited by a given set of characters
     * @param delimiter String containing all the characters that delimit the given string of actions
     */
    public Evolution(int[] actionList) {
        actions = actionList;
        init();
    }

    /**
     *
     * <p>Retrieves all the currently generated list of all possible DNA to be
     *  scored. As the user retrieves all the DNA, each one should be inspected
     *  and given a score using {@link DNA#setScore(double) setScore}. this score
     *  should be a reflection upon the performance of the DNA in the environment.</p>
     *
     * <p>Warning: All scores that return a positive influence in the environment should
     *  be given a score of more then 0. Any DNA with a score less then 0 will be
     *  culled instantly and not used for reproduction, unless every score is less
     *  then 0. This is a dangerous method prone to large amount of errors,
     *  so negative scores should be avoided at all cost.</p>
     *
     * <p>After all the DNA in the list has been scored, then a new generation can be
     *  created by using {@link produceNextGeneration() produceNextGeneration}. Any
     *  DNA that does not have a score will have an assumed score of 0.</p>
     *
     * @return A list of all the currently generated combinations of DNA
     */
    public DNA[] getAllDNA() {
        return pathStore;
    }

    /**
     * <p>Sets the amount of children to be created initially and at each new
     * generation.</p>
     *
     * <p>When this method is invoked a new <b>random</b> generation of <b>DNA</b> will be
     * created, so this method should <b>only</b> be called at the <b>beginning.</b><p>
     *
     * <p>Every time {@link ArtificialIntelligence#produceNextGeneration() produceNextGeneration} is used,
     * a new generation of children of the given quantity will be produced.</p>
     *
     * <p>By default the method of reproduction is {@link ArtificialIntelligence#MEIOSIS Meiosis} but this
     *  can be changed to {@link ArtificialIntelligence#MITOSIS Mitosis} by using
     *  {@link ArtificialIntelligence#setReproductionMethod(int) setReproductionMethod}</p>
     *
     * <p>If meiosis is being used, the optimum number of children is usually between 40 and 500, depending
     *  on how many possible chunks of DNA there are. If in doubt, 100 is usually a safe consistent number for
     *  all meiosis reproduction.</p>
     *
     * </p>If mitosis is being used, the optimum number of children is usually between 500 and 2000, depending
     *  on how many possible chunks of DNA there are. If in doubt, 1000 is usually a safe consistent number for
     *  all mitosis reproduction.</p>
     *
     * <p>If you are still unsure on what number to have, you can leave it to the
     * default (which assumes meiosis is occurring).
     * <br/>The default number of children is 100.</p>
     *
     * @param children
     */
    public void setNumberOfChildren(int children) {
        NUMBER_OF_CHILDREN = children;
        init();
    }

    /**
     * <p>Produces the next generation of children using the set variables</p>
     *
     *  <p>To produce more or less children then the default 10, use {@link ArtificialIntelligence#setNumberOfChildren(int) setNumberOfChildren}
     *  <br/>To change how {@link DNA DNA} is inherited, use {@link ArtificialIntelligence#setReproductionMethod(int) setReproductionMethod}</p>
     *
     * <p>When new children are generated they automatically undergo mutation,
     *  mutation itself is not optional, but the chances of mutation occurring can be modified.</p>
     *
     * <p>When mutation occurs it changes the DNA in one of three ways. Each way has a
     *  random chance of occurring due to the variables set. These ways are:
     * <br/><ul>
     * <li>{@link ArtificialIntelligence#INSERTION Insertion}</li>
     * <li>{@link ArtificialIntelligence#DELETION Deletion}</li>
     * <li>{@link ArtificialIntelligence#ALTERATION Alteration}</li>
     * </ul></p>
     *
     * <p>You can modify the chances of each occurring by using {@link ArtificialIntelligence#set(int, int) set(TYPE_OF_MUTATION, CHANCE_OF_OCCURRING)}</p>
     *
     * <p>The default chance of each occurring is 50%</p>
     *
     */
    public void produceNextGeneration() {
        DNA best = new DNA(), secondBest = new DNA(), dna;
        for (int i = 0; i < NUMBER_OF_CHILDREN; i++) {
            dna = pathStore[i];
            if (dna.getScore() > best.getScore()) {
                secondBest = best;
                best = dna;
            }
             else if (dna.getScore() > secondBest.getScore()) {
                secondBest = dna;
             }
        }

        best = new DNA(best.getDNA());
        secondBest = new DNA(secondBest.getDNA());

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            crunchers[i] = new ChildCruncher();
            crunchers[i].setParents(best, secondBest);
            crunchers[i].setPeriod(i*(NUMBER_OF_CHILDREN/NUMBER_OF_THREADS), NUMBER_OF_CHILDREN/NUMBER_OF_THREADS);
            crunchers[i].setStore(pathStore);
            crunchers[i].start();
        }

    }

    /**
     * <p>Sets the reproduction method to a specified method.
     * <br/>This changes how each new cycle of children are created.</p>
     *
     * <p>Possible methods are:
     * <br/><ul>
     * <li>{@link ArtificialIntelligence#MEIOSIS Meiosis}</li>
     * <li>{@link ArtificialIntelligence#MITOSIS Mitosis}</li>
     * </ul></p>
     *
     * <p>The default method is Meiosis.</p>
     *
     * @param type Type of reproduction method to set this AI to.
     */
    public static void setReproductionMethod(int type) {
        if (type == MEIOSIS) BEST_PARENT_BIAS = 0;
        if (type == MITOSIS) BEST_PARENT_BIAS = 50;
    }

    public static int rand(int i) {
        return (int)(Math.random()*i);
    }

    public static int[] mutate(int[] path) {
        // Three types of mutations
        // Insertion -- Adding a random element in a random position
        // Deletion -- Deleting a random position
        // Alteration -- Changing a random position to something else
        if (path.length == 0) return path;
        if (rand100.nextNumber() < INSERTION_CHANCE) {
            int insertion = actions[rand(actions.length)];
            int position = rand(path.length);
            int[] newPath = new int[path.length+1];
            System.arraycopy(path, 0, newPath, 0, position);
            newPath[position] = insertion;
            System.arraycopy(path, position, newPath, position+1, path.length-position);
            path = newPath;
        }
        if (path.length == 0) return path;
        if (rand100.nextNumber() < DELETION_CHANCE) {
            int position = rand(path.length);
            int[] newPath = new int[path.length-1];
            System.arraycopy(path, 0, newPath, 0, position);
            System.arraycopy(path, position+1, newPath, position, path.length-(position+1));
            path = newPath;
        }
        if (path.length == 0) return path;
        if (rand100.nextNumber() < ALTERATION_CHANCE) {
            int alteration = actions[rand(actions.length)];
            int position = rand(path.length);
            path[position] = alteration;
        }
        return path;
    }

    private void init() {
        rand100 = new RandomNumberGenerator(0, 100);
        crunchers = new ChildCruncher[NUMBER_OF_THREADS];
        pathStore = new DNA[NUMBER_OF_CHILDREN];
        for (int i = 0; i < NUMBER_OF_CHILDREN; i++) {
            int[] path = new int[LENGTH_OF_DNA];
            for (int j = 0; j < LENGTH_OF_DNA; j++) path[j] = actions[rand(actions.length)];
            pathStore[i] = new DNA(path);
        }
    }

    /**
     * <p>Sets a given variable to a given value</p>
     * <p>Variables that can be set this way are:
     *  <br/><ul>
     * <li>{@link ArtificialIntelligence#INSERTION Insertion}</li>
     * <li>{@link ArtificialIntelligence#DELETION Deletion}</li>
     * <li>{@link ArtificialIntelligence#ALTERATION Alteration}</li>
     * </ul></p>
     *
     * @param variable Variable to be manually set
     * @param value Value to set the variable to
     */
    public void set(int variable, int value) {
        if (variable == INSERTION) INSERTION_CHANCE = value;
        if (variable == DELETION) DELETION_CHANCE = value;
        if (variable == ALTERATION) ALTERATION_CHANCE = value;
        if (variable == LENGTH) {LENGTH_OF_DNA = value; init();}
    }

}

