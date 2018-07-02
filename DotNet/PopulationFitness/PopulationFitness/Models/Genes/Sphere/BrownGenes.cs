using PopulationFitness.Models.Genes.BitSet;
using System;

namespace PopulationFitness.Models.Genes.Sphere
{
    public class BrownGenes : NormalizingBitSetGenes
    {
        public BrownGenes(Config config) : base(config, 1.0)
        {
        }

        protected override double CalculateNormalizationRatio(int n)
        {
            return n > 1 ? 2.0 * (n - 1) : 1.0;
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              f left (x right ) = sum from {i=1} to {n-1} {left [{left ({x} rsub {i} rsup {2} right )} ^ {{x} rsub {i+1} rsup {2} +1} + {left ({x} rsub {i+1} rsup {2} right )} ^ {{x} rsub {i} rsup {2} +1} right ]}
             */

            if (integer_values.Length < 1)
            {
                return 0.0;
            }

            double fitness = 0.0;

            double xN = Interpolate(integer_values[0]);
            double xNSquared = xN * xN;

            if (integer_values.Length < 2)
            {
                return xNSquared;
            }

            for (int i = 0; i < integer_values.Length - 1; i++)
            {
                double xNPlus1 = Interpolate(integer_values[i + 1]);
                double xNPlus1Squared = xNPlus1 * xNPlus1;
                fitness += Math.Pow(xNSquared, xNPlus1Squared + 1.0) + Math.Pow(xNPlus1Squared, xNSquared + 1.0);

                xNSquared = xNPlus1Squared;
            }

            return fitness;
        }
    }
}
