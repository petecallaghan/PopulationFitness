using System;

namespace PopulationFitness.Models
{

    /**
     * Provides a single sequence of random numbers from a repeatable seed.
     *
     * Created by pete.callaghan on 07/07/2017.
     */
    public class RepeatableRandom
    {
        private const int DEFAULT_SEED = 31;

        private static Random random = new Random(DEFAULT_SEED);

        /**
         * Changes the seed for the sequence. Call this before generating any random numbers
         *
         * @param seed
         */
        public static void SetSeed(long seed)
        {
            random = new Random((int)seed);
        }

        /**
         * Resets the seed to the default
         */
        public static void ResetSeed()
        {
            random = new Random(DEFAULT_SEED);
        }

        /**
         *
         * @return the next random number between 0 (inclusive) and 1 (exclusive).
         */
        public static double GenerateNext()
        {
            return random.NextDouble();
        }

        /**
         * @param range the range
         * @return the next random number between 0 (inclusive) and range  (exclusive)
         */
        public static int GenerateNextInt(double range)
        {
            return (int)(random.NextDouble() * range);
        }

        public static long GenerateNextLong(long min, long max)
        {
            return min + (long)(random.NextDouble() * (max - min + 1));
        }

        public static Random GetRandom()
        {
            return random;
        }
    }
}
