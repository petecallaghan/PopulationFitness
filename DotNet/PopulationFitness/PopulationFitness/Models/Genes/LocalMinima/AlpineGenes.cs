using PopulationFitness.Models.FastMaths;
using PopulationFitness.Models.Genes.BitSet;
using System;

namespace PopulationFitness.Models.Genes.LocalMinima
{
    public class AlpineGenes : NormalizingBitSetGenes
    {

        public AlpineGenes(Config config) : base(config, 10.0) { }

        protected override double CalculateNormalizationRatio(int n)
        {
            return 8.7149 * n;
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {

            /*
              f left (x right ) = sum from {i=1} to {n} {left lline {x} rsub {i} sin {left ({x} rsub {i} right ) +0.1 {x} rsub {i}} right rline}
             */

            double fitness = 0.0;

            foreach (long integer_value in integer_values)
            {
                double x = Interpolate(integer_value);
                fitness += Math.Abs(x * CosSineCache.Sin(x) + 0.1 * x);
            }

            return fitness;
        }
    }
}
