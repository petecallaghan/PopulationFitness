using PopulationFitness.Models.FastMaths;
using PopulationFitness.Models.Genes.BitSet;

namespace PopulationFitness.Models.Genes.Sphere
{
    public class QingGenes : NormalizingBitSetGenes
    {
        private class NormalizationRatioCalculator : IValueCalculator<double>
        {

            public double CalculateValue(long n)
            {
                double ratio = 0.0;
                for (int i = 1; i <= n; i++)
                {
                    double value = 250000.0 - i;
                    ratio += value * value;
                }
                return n > 0 ? ratio : 1.0;
            }
        }

        private static readonly ExpensiveCalculatedValues<double> NormalizationRatios = new ExpensiveCalculatedValues<double>(new QingGenes.NormalizationRatioCalculator());

        public QingGenes(Config config) : base(config, 500.0)
        {
        }

        protected override double CalculateNormalizationRatio(int n)
        {
            return NormalizationRatios.FindOrCalculate(n);
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              f left (x right ) = sum from {i=1} to {n} {{left ({x} rsub {i} rsup {2} -i right )} ^ {2}}
             */

            double fitness = 0.0;

            for (int i = 0; i < integer_values.Length; i++)
            {
                double x = Interpolate(integer_values[i]);
                double value = x * x - (i + 1);
                fitness += value * value;
            }
            return fitness;
        }
    }
}
