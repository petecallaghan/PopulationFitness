using PopulationFitness.Models.Genes.BitSet;
using System;

namespace PopulationFitness.Models.Genes.Sphere
{
    public class GriewankGenes : NormalizingBitSetGenes
    {
        public GriewankGenes(Config config) : base(config, 600.0)
        { }

        protected override double CalculateNormalizationRatio(int n)
        {
            return 900.0 * n + 2.0;
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              f left (x right ) = sum from {i=1} to {n} {{{x} rsub {i} rsup {2}} over {400} - prod from {i=1} to {n} {cos {left ({{x} rsub {i}} over {sqrt {i}} right )} +1}}
             */

            if (integer_values.Length < 1) return 0.0;

            double fitness = 1.0;
            double product = 1.0;

            for (int i = 1; i < integer_values.Length; i++)
            {
                double x = Interpolate(integer_values[i]);

                product *= FastMaths.CosSineCache.Cos(x / Math.Sqrt(1.0 * i));
                fitness += (x * x / 400.0);
            }
            return fitness + product;
        }
    }
}
