using PopulationFitness.Models.FastMaths;
using PopulationFitness.Models.Genes.BitSet;
using System;
using System.Collections.Generic;
using System.Text;

namespace PopulationFitness.Models.Genes.LocalMinima
{
    public class Schwefel226Genes : NormalizingBitSetGenes
    {

        private const double SchwefelConstant = 418.982;

        private const double SchwefelConstant2 = 420.9687;

        private class NormalizationRatioCalculator : IValueCalculator<double>
        {
            public double CalculateValue(long n)
            {
                return SchwefelConstant * n + SchwefelConstant2 * CosSineCache.Sin(Math.Sqrt(SchwefelConstant2)) * n;
            }
        }

        private static readonly ExpensiveCalculatedValues<double> NormalizationRatios = new ExpensiveCalculatedValues<double>(new NormalizationRatioCalculator());

        public Schwefel226Genes(Config config) : base(config, 50000.0) { }

        protected override double CalculateNormalizationRatio(int n)
        {
            return NormalizationRatios.FindOrCalculate(n);
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              http://www.sfu.ca/~ssurjano/schwef.html

              f(x) = 418.9829d - sum{i=1 to d}[x{i} * sin(sqrt(mod(x{i})))]

              Dimensions: d

             The Schwefel226 function is complex, with many local minima. The plot shows the two-dimensional form of the function.

             Input Domain:

             The function is usually evaluated on the hypercube xi ∈ [-500, 500], for all i = 1, …, d.

             Global Minimum:

             f(x) = 0, at x = (420.9687,...,420.9687)

             */
            double fitness = SchwefelConstant * integer_values.Length;

            foreach (long integer_value in integer_values)
            {
                double x = Interpolate(integer_value);
                fitness -= (x * CosSineCache.Sin(Math.Sqrt(Math.Abs(x))));
            }
            return fitness;
        }
    }
}
