using PopulationFitness.Models.FastMaths;
using PopulationFitness.Models.Genes.BitSet;
using System;

namespace PopulationFitness.Models.Genes.Sphere
{
    public class ExponentialGenes : NormalizingBitSetGenes
    {
        private class NormalizationRatioCalculator : IValueCalculator<Double>
        {
            public Double CalculateValue(long n)
            {
                return 1.0 - Math.Exp(-0.5 * n);
            }
        }

        private static readonly ExpensiveCalculatedValues<double> NormalizationRatios = new ExpensiveCalculatedValues<double>(new ExponentialGenes.NormalizationRatioCalculator());

        public ExponentialGenes(Config config) : base(config, 1.0)
        {
        }

        protected override double CalculateNormalizationRatio(int n)
        {
            return NormalizationRatios.FindOrCalculate(n);
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              f left (x right ) =- exp left (-0.5 sum from {i=2} to {n} {{x} rsub {i} rsup {2}} right )
             */

            double fitness = 0.0;

            foreach (long integer_value in integer_values)
            {
                double x = Interpolate(integer_value);

                fitness += x * x;
            }

            return 1.0 - Math.Exp(-0.5 * fitness);
        }
    }
}
