package uk.edu.populationfitness.models;

import java.util.SplittableRandom;

/**
 * Provides a single sequence of random numbers from a repeatable seed.
 *
 * Created by pete.callaghan on 07/07/2017.
 */
public class RepeatableRandom {
    private static final long DEFAULT_SEED = 31l;

    private static SplittableRandom random = new SplittableRandom(DEFAULT_SEED);

    /**
     * Changes the seed for the sequence. Call this before generating any random numbers
     *
     * @param seed
     */
    public static void setSeed(long seed){
        random = new SplittableRandom(seed);
    }

    /**
     * Resets the seed to the default
     */
    public static void resetSeed(){
        random = new SplittableRandom(DEFAULT_SEED);
    }

    public static double generateNext(){
        return random.nextDouble();
    }
}
