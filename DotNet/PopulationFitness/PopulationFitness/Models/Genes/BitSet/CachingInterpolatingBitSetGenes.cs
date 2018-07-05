namespace PopulationFitness.Models.Genes.BitSet
{
    /**
     * Only calculates the fitness if the fitness factor has changed.
     */
    public abstract class CachingInterpolatingBitSetGenes : InterpolatingBitSetGenes
    {

        protected CachingInterpolatingBitSetGenes(Config config, double maxInterpolatedValue) : base(config, maxInterpolatedValue)
        {
        }

        /**
         * Implement this to calculate the fitness from an array of integers.
         *
         * @param integer_values
         * @return
         */
        protected abstract double CalculateFitnessFromIntegers(long[] integer_values);

        public override double Fitness
        {
            get
            {
                if (IsFitnessStored())
                {
                    return StoredFitness();
                }

                return StoreInvertedFitness(CalculateFitnessFromIntegers(AsIntegers));
            }
        }
    }
}
