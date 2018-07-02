using PopulationFitness.Models.Genes.BitSet;
using System;

namespace PopulationFitness.Models.Genes.Sphere
{
    public class SumOfPowersGenes : NormalizingBitSetGenes
    {
        public SumOfPowersGenes(Config config) : base(config, 1.0)
        { }

        protected override double CalculateNormalizationRatio(int n)
        {
            return 1.0;
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              http://www.sfu.ca/~ssurjano/sumpow.html

              f(x) = sum{i=1 to d}[ abs(x{i}) ^ i+1]

              Dimensions: d

             The function is unimodal.

             Input Domain:

             The function is usually evaluated on the hypercube xi ∈ [-1, 1], for all i = 1, …, d.

             Global Minimum:

             f(x) = 0, at x = (0,...,0)

             */
            double fitness = 0;

            for (int i = 0; i < integer_values.Length; i++)
            {
                double x = Interpolate(integer_values[i]);
                fitness += FastMaths.FastMaths.Pow(Math.Abs(x), i + 2);
            }
            return fitness;
        }
    }
}
