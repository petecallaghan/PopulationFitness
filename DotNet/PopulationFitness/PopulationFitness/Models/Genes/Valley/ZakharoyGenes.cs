using PopulationFitness.Models.FastMaths;
using PopulationFitness.Models.Genes.BitSet;

namespace PopulationFitness.Models.Genes.Valley
{
    public class ZakharoyGenes : NormalizingBitSetGenes
    {
        private class NormalizationRatioCalculator : IValueCalculator<double>
        {
            public double CalculateValue(long n)
            {
                double value = (n * (n + 1.0)) / 2.0;
                return 100 * n + 25 * (value * value + 35.0 * FastMaths.FastMaths.Pow(value, 3));
            }
        }

        private static readonly ExpensiveCalculatedValues<double> NormalizationRatios = new ExpensiveCalculatedValues<double>(new NormalizationRatioCalculator());

        public ZakharoyGenes(Config config) : base(config, 10.0)
        { }

        protected override double CalculateNormalizationRatio(int n)
        {
            return NormalizationRatios.FindOrCalculate(n);
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              f left (x right ) = sum from {i=1} to {n} {{x} rsub {i} rsup {2}} + {left (sum from {i=1} to {n} {0.5 i  {x} rsub {i}} right )} ^ {2} + {left (sum from {i=1} to {n} {0.5 i  {x} rsub {i}} right )} ^ {4}
             */
            double fitness = 0;

            double sum = 0;

            for (int i = 1; i <= integer_values.Length; i++)
            {
                double x = Interpolate(integer_values[i - 1]);
                fitness += x * x;
                sum += 0.5 * i * x;
            }

            fitness += (sum * sum) + (sum * sum * sum * sum);

            return fitness;
        }
    }
}
