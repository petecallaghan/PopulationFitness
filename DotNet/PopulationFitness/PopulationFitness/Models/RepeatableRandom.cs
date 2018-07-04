using System;
using System.Threading;

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

        private static int _seed = DEFAULT_SEED;

        private static readonly object Lock = new object();

        private static int Seed
        {
            get
            {
                lock (Lock)
                {
                    return _seed;
                }
            }
            set
            {
                lock (Lock)
                {
                    _seed = value;
                }
            }
        }

        private static ThreadLocal<Random> random = new ThreadLocal<Random>(() => { return new Random(Seed); });

        /**
         * Changes the seed for the sequence. Call this before generating any random numbers
         *
         * @param seed
         */
        public static void SetSeed(long seed)
        {
            Seed = (int)seed;
            random.Value = new Random(Seed);
        }

        /**
         * Resets the seed to the default
         */
        public static void ResetSeed()
        {
            Seed = DEFAULT_SEED;
            random.Value = new Random(Seed);
        }

        /**
         *
         * @return the next random number between 0 (inclusive) and 1 (exclusive).
         */
        public static double GenerateNext()
        {
            return random.Value.NextDouble();
        }

        /**
         * @param range the range
         * @return the next random number between 0 (inclusive) and range  (exclusive)
         */
        public static int GenerateNextInt(double range)
        {
            return (int)(random.Value.NextDouble() * range);
        }

        public static long GenerateNextLong(long min, long max)
        {
            return min + (long)(random.Value.NextDouble() * (max - min + 1));
        }

        public static Random GetRandom()
        {
            return random.Value;
        }
    }
}
