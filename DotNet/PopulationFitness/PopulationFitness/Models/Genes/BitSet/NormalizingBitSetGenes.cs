namespace PopulationFitness.Models.Genes.BitSet
{

    /**
     * Adds normalizing of fitness to caching and interpolation.
     */
    public abstract class NormalizingBitSetGenes : CachingInterpolatingBitSetGenes
    {
        private double _normalizationRatio;
        private bool _isNormalisationRatioSet;

        protected NormalizingBitSetGenes(Config config, double maxInterpolatedValue) : base(config, maxInterpolatedValue)
        {
            _isNormalisationRatioSet = false;
        }

        /***
         * Implement this to define the normalization ratio
         * @param n
         * @return the normalization ratio
         */
        protected abstract double CalculateNormalizationRatio(int n);

        public override double Fitness
        {
            get
            {
                if (IsFitnessStored())
                {
                    return StoredFitness();
                }

                long[] integers = AsIntegers;

                if (!_isNormalisationRatioSet)
                {
                    _normalizationRatio = CalculateNormalizationRatio(integers.Length);
                    _isNormalisationRatioSet = true;
                }

                return StoreFitness(CalculateFitnessFromIntegers(integers) / _normalizationRatio);
            }
        }
    }
}
