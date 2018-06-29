namespace PopulationFitness.Models.Genes.BitSet
{

    /**
     * Adds normalizing of fitness to caching and interpolation.
     */
    public abstract class NormalizingBitSetGenes : CachingInterpolatingBitSetGenes
    {

        private double normalizationRatio;

        private bool isNormalisationRatioSet;

        protected NormalizingBitSetGenes(Config config, double maxInterpolatedValue) : base(config, maxInterpolatedValue)
        {
            isNormalisationRatioSet = false;
        }

        /***
         * Implement this to define the normalization ratio
         * @param n
         * @return the normalization ratio
         */
        protected abstract double CalculateNormalizationRatio(int n);

        public override double Fitness()
        {
            if (IsFitnessStored())
            {
                return StoredFitness();
            }

            long[] integers = AsIntegers();

            if (!isNormalisationRatioSet)
            {
                normalizationRatio = CalculateNormalizationRatio(integers.Length);
                isNormalisationRatioSet = true;
            }

            return StoreFitness(CalculateFitnessFromIntegers(integers) / normalizationRatio);
        }
    }
}
