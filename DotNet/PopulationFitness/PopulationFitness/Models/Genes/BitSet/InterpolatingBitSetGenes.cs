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

        private static ExpensiveCalculatedValues<long> BitCounts = new ExpensiveCalculatedValues<long>(new InterpolatingBitSetGenes.BitCountCalculator());

        protected double interpolation_ratio;

        /**
         * Calculates the maximum value given the bit count
         *
         * @param bitCount
         * @return
         */
        private static long MaxForBits(long bitCount)
        {
            return BitCounts.FindOrCalculate(bitCount);
        }

        protected InterpolatingBitSetGenes(Config config, double maxInterpolatedValue) : base(config)
        {
            interpolation_ratio = maxInterpolatedValue / MaxLongForSizeOfGene();
        }

        /**
         *
         * @return the maximum long value given the size of the genes
         */
        protected long MaxLongForSizeOfGene()
        {
            return MaxForBits(NumberOfBits());
        }

        protected double Interpolate(long integer_value)
        {
            return interpolation_ratio * integer_value;
        }
    }
}
