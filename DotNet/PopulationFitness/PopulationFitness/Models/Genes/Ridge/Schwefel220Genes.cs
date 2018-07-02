using PopulationFitness.Models.Genes.BitSet;
using System;

namespace PopulationFitness.Models.Genes.Ridge
{
    public class Schwefel220Genes : NormalizingBitSetGenes
    {
        public Schwefel220Genes(Config config) : base(config, 10.0)
        {
        }

        protected override double CalculateNormalizationRatio(int n)
        {
            return 10.0 * n;
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {

            /*
              f left (x right ) =- sum from {i=1} to {n} {left lline {x} rsub {i} right rline}
             */
            double fitness = 10.0 * integer_values.Length;

            foreach (long integer_value in integer_values)
            {
                double x = Interpolate(integer_value);
                fitness -= Math.Abs(x);
            }

            return fitness;
        }
    }
}
