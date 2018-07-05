using PopulationFitness.Models.FastMaths;
using PopulationFitness.Models.Genes.BitSet;
using System;

namespace PopulationFitness.Models.Genes.Valley
{
    public class TridGenes : NormalizingBitSetGenes
    {
        private class MinCalculator : IValueCalculator<double>
        {

            public double CalculateValue(long index)
            {
                double min = (0.0 - index) * (index + 4.0) * (index + 1.0);
                return min / 6;
            }
        }

        private class MaxCalculator : IValueCalculator<double>
        {

            public double CalculateValue(long index)
            {
                /*
                  M= left [{n} over {2} right ] {( {n} ^ {2} +1)} ^ {2} + left (n- left [{n} over {2} right ] right ) {( {n} ^ {2} -1)} ^ {2} + left (n-1 right ) {n} ^ {4}

                 */
                double nOver2 = Math.Round(((double)index) / 2.0);
                long nSquared = index * index;

                return nOver2 * FastMaths.FastMaths.Pow(nSquared + 1, 2) +
                        (index - nOver2) * FastMaths.FastMaths.Pow(nSquared - 1, 2) +
                        (index - 1) * nSquared * nSquared;
            }
        }

        private static readonly ExpensiveCalculatedValues<double> CachedMinValues = new ExpensiveCalculatedValues<double>(new MinCalculator());
        private static readonly ExpensiveCalculatedValues<double> CachedMaxValues = new ExpensiveCalculatedValues<double>(new MaxCalculator());

        private double min;

        public TridGenes(Config config) : base(config, 0.0)
        { }

        /**
         * Calculates the interpolation values and the normalization values
         * @param n
         * @return
         */
        protected override double CalculateNormalizationRatio(int n)
        {
            min = CachedMinValues.FindOrCalculate(n);
            double max = CachedMaxValues.FindOrCalculate(n);
            CalculateInterpolationRatio(n);
            return max - min;
        }

        private void CalculateInterpolationRatio(int n)
        {
            /*
              {- {n} ^ {2} ≤x} rsub {i} ≤+ {n} ^ {2}
             */
            _interpolationRatio = (1.0 * n * n) / MaxLongForSizeOfGene;
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              https://www.sfu.ca/~ssurjano/trid.html

              f left (x right ) = sum from {i=1} to {n} {{left ({x} rsub {i} -1 right )} ^ {2} -} sum from {i=2} to {n} {{x} rsub {i} {x} rsub {i-1}}
             */
            double fitness = 0.0 - min;

            if (integer_values.Length > 0)
            {
                double previousX = Interpolate(integer_values[0]);
                double firstSum = FastMaths.FastMaths.Pow(previousX - 1, 2);
                double secondSum = 0;
                for (int i = 1; i < integer_values.Length; i++)
                {
                    double x = Interpolate(integer_values[i]);
                    firstSum += (x - 1) * (x - 1);
                    secondSum += x * previousX;
                    previousX = x;
                }
                fitness += (firstSum - secondSum);
            }
            return fitness;
        }
    }
}
