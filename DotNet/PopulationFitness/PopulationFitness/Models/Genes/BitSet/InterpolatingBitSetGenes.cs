using PopulationFitness.Models.FastMaths;
using System;

namespace PopulationFitness.Models.Genes.BitSet
{
    public abstract class InterpolatingBitSetGenes : InvertedBitSetGenes
    {
        private class BitCountCalculator : IValueCalculator<long>
        {
            public long CalculateValue(long bitCount)
            {
                return Math.Min(long.MaxValue, (long)FastMaths.FastMaths.Pow(2, bitCount) - 1);
            }
        }

        private static ExpensiveCalculatedValues<long> _bitCounts = new ExpensiveCalculatedValues<long>(new InterpolatingBitSetGenes.BitCountCalculator());

        protected double _interpolationRatio;

        /**
         * Calculates the maximum value given the bit count
         *
         * @param bitCount
         * @return
         */
        private static long MaxForBits(long bitCount)
        {
            return _bitCounts.FindOrCalculate(bitCount);
        }

        protected InterpolatingBitSetGenes(Config config, double maxInterpolatedValue) : base(config)
        {
            _interpolationRatio = maxInterpolatedValue / MaxLongForSizeOfGene;
        }

        /**
         *
         * The maximum long value given the size of the genes
         */
        protected long MaxLongForSizeOfGene
        {
            get
            {
                return MaxForBits(NumberOfBits);
            }
        }

        protected double Interpolate(long integer_value)
        {
            return _interpolationRatio * integer_value;
        }
    }
}
