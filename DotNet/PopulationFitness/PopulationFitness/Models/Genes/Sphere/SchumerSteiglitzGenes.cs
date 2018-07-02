using PopulationFitness.Models.FastMaths;
using PopulationFitness.Models.Genes.BitSet;

namespace PopulationFitness.Models.Genes.Sphere
{
    public class SchumerSteiglitzGenes : NormalizingBitSetGenes
    {
        private class NormalizationRatioCalculator : IValueCalculator<double>
        {

            public double CalculateValue(long n)
            {
                return 100000000.0 * n;
            }
        }

        private static readonly ExpensiveCalculatedValues<double> NormalizationRatios = new ExpensiveCalculatedValues<double>(new SchumerSteiglitzGenes.NormalizationRatioCalculator());

        public SchumerSteiglitzGenes(Config config) : base(config, 100.0)
        { }

        protected override double CalculateNormalizationRatio(int n)
        {
            return NormalizationRatios.FindOrCalculate(n);
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              f left (x right ) = sum from {i=1} to {n} {{x} rsub {i} rsup {4}}
             */

            double fitness = 0.0;

            foreach (long integer_value in integer_values)
            {
                double x = Interpolate(integer_value);

                fitness += FastMaths.FastMaths.Pow(x, 4);
            }

            return fitness;
        }
    }
}
