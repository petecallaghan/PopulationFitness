using PopulationFitness.Models.FastMaths;
using PopulationFitness.Models.Genes.BitSet;
using System;

namespace PopulationFitness.Models.Genes.LocalMinima
{
    public class SalomonGenes : NormalizingBitSetGenes
    {
        private const double NormalizationConstant = 200.0 * Math.PI + 10.0;

        private const double TwoPi = 2.0 * Math.PI;

        public SalomonGenes(Config config) : base(config, 100.0) { }

        protected override double CalculateNormalizationRatio(int n)
        {
            return 1.0 - CosSineCache.Cos(NormalizationConstant * Math.Sqrt(n));
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              f left (x right ) =1- cos {left (2π sqrt {sum from {i=1} to {n} {{x} rsub {i} rsup {2}}} right ) +0.1 sqrt {sum from {i=1} to {n} {{x} rsub {i} rsup {2}}}}
             */

            double sum = 0.0;

            foreach (long integer_value in integer_values)
            {
                double x = Interpolate(integer_value);
                sum += x * x;
            }

            double sqrtSum = Math.Sqrt(sum);

            return 1.0 - CosSineCache.Cos(TwoPi * sqrtSum) + 0.1 * sqrtSum;
        }
    }
}